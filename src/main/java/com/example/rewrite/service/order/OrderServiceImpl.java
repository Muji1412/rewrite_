package com.example.rewrite.service.order;

import com.example.rewrite.entity.Cart;
import com.example.rewrite.entity.OrderCart;
import com.example.rewrite.entity.Orders;
import com.example.rewrite.entity.Product;
import com.example.rewrite.repository.cart.CartRepository;
import com.example.rewrite.repository.order.OrderRepository;
import com.example.rewrite.repository.ordercart.OrderCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository ordersRepository;

    @Autowired
    private OrderCartRepository orderCartRepository;
    @Autowired
    private CartRepository cartRepository;

    @Transactional
    @Override
    public void saveOrder(Orders order, List<Cart> checkedCarts) {
        // 주문 저장
        Orders savedOrder = ordersRepository.save(order);

        // OrderCart 저장
        List<OrderCart> orderCarts = checkedCarts.stream()
                .map(cart -> OrderCart.builder()
                        .orders(savedOrder)
                        .product(cart.getProduct())
                        .price(Integer.parseInt(cart.getProduct().getPrice()))
                        .build())
                .collect(Collectors.toList());

        orderCartRepository.saveAll(orderCarts);  // OrderCart 목록을 한 번에 저장
    }
    @Transactional
    public void updateOrderWithPaymentInfo(String tossOrderId, String paymentKey,
                                           int amount, String paymentMethod,
                                           String paymentStatus, String approvedAt) {
        // tossOrderId로 주문 찾기
        Orders order = ordersRepository.findByTossOrderId(tossOrderId)
                .orElseThrow(() -> new RuntimeException("주문 정보를 찾을 수 없습니다."));

        // 결제 정보 업데이트
        order.setPaymentKey(paymentKey);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus("결제완료"); // 또는 paymentStatus 사용

        try {
            // ISO 8601 형식의 시간 문자열 처리
            LocalDateTime dateTime = LocalDateTime.parse(
                    approvedAt.replace("+09:00", ""),
                    DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            order.setPaidAt(Timestamp.valueOf(dateTime).toLocalDateTime());
        } catch (Exception e) {
            // 시간 변환 오류 시 현재 시간으로 설정
            order.setPaidAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
        }

        // 주문 상태 업데이트
        order.setOrderStatus("결제완료");

        // DB에 저장
        ordersRepository.save(order);

        // 필요하다면 여기서 이메일 알림 등 추가 작업 수행
    }



    @Override
    public List<Orders> getOrderList(Long uid) {
        return ordersRepository.findByBuyerUid(uid);
    }

    @Override
    public List<OrderCart> getOrderDetail(Long uid) {

        return ordersRepository.findByOrderId(uid);
    }

    @Override
    public List<OrderCart> findOrderCartsByBuyerUid(Long uid) {
        return ordersRepository.findOrderCartsByBuyerUid(uid);
    }

    @Override
    public List<Product> findOrderDetail(Long oid) {
        return ordersRepository.findOrderDetail(oid);
    }

    @Override
    public Orders findByOrderId(Long oid) {
        return ordersRepository.getOrder(oid);
    }
}
