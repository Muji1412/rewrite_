package com.example.rewrite.service.order;

import com.example.rewrite.entity.Cart;
import com.example.rewrite.entity.OrderCart;
import com.example.rewrite.entity.Orders;
import com.example.rewrite.repository.cart.CartRepository;
import com.example.rewrite.repository.order.OrderRepository;
import com.example.rewrite.repository.ordercart.OrderCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
