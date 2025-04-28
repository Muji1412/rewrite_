package com.example.rewrite.service.order;

import com.example.rewrite.entity.Cart;
import com.example.rewrite.entity.Orders;
import com.example.rewrite.entity.OrderCart;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    void saveOrder(Orders order, List<Cart> checkedCarts); //order에 저장

}

