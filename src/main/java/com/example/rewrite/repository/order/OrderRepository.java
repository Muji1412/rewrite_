package com.example.rewrite.repository.order;


import com.example.rewrite.entity.OrderCart;
import com.example.rewrite.entity.Orders;
import com.example.rewrite.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    // 특정 사용자의 모든 주문 조회
    List<Orders> findByBuyerUid(Long uid);
    List<OrderCart> findByOrderId(Long oid);

    @Query("SELECT oc FROM OrderCart oc " +
            "JOIN FETCH oc.orders o " +
            "JOIN FETCH oc.product p " +
            "WHERE o.buyer.uid = :uid")
    List<OrderCart> findOrderCartsByBuyerUid(@Param("uid") Long uid);

    //특정 order의 모든 product 조회
    @Query("SELECT p FROM OrderCart oc JOIN oc.product p WHERE oc.orders.orderId = :orderId")
    List<Product> findOrderDetail(@Param("orderId") Long orderId);

    //특정 order 조회
    @Query("SELECT o FROM Orders o WHERE o.orderId = :orderId")
    Orders getOrder(@Param("orderId") Long oid);

    //내 order의 product 전체 조회
    @Query("SELECT p FROM OrderCart oc JOIN oc.product p JOIN oc.orders o WHERE o.buyer.uid = :uid")
    List<Product> getOrderAll(@Param("uid")Long uid, Pageable pageable);

    Optional<Orders> findByTossOrderId(String tossOrderId);
}
