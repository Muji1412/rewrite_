package com.example.rewrite.repository.order;


import com.example.rewrite.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {


}
