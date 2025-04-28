package com.example.rewrite.repository.ordercart;

import com.example.rewrite.entity.OrderCart;
import com.example.rewrite.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCartRepository extends JpaRepository<OrderCart, Long> {

    List<OrderCart> findByOrders(Orders orders);

}
