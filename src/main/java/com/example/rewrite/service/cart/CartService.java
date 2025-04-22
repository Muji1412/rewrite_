package com.example.rewrite.service.cart;

import com.example.rewrite.command.user.UserSessionDto;
import com.example.rewrite.entity.Cart;
import com.example.rewrite.entity.Product;
import com.example.rewrite.entity.Users;

import java.util.List;

public interface CartService {
    int calculateTotalPrice(List<Cart> cartList);
    void addCart(Long uid, Long prodId);
    List<Cart> getCarts(Long uid);
    void deleteCart(Long cartId, Long uid);

}
