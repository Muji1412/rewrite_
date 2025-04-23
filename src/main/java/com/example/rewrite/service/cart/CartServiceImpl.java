package com.example.rewrite.service.cart;

import com.example.rewrite.command.user.UserSessionDto;
import com.example.rewrite.entity.Cart;
import com.example.rewrite.entity.Product;
import com.example.rewrite.entity.Users;
import com.example.rewrite.repository.cart.CartRepository;
import com.example.rewrite.repository.product.ProductRepository;
import com.example.rewrite.repository.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UsersRepository usersRepository;


    @Override
    public int calculateTotalPrice(List<Cart> cartList) {
        int totalPrice = 0;

        for (Cart cart : cartList) {
            int price = Integer.parseInt(cart.getProduct().getPrice());
            totalPrice += price;
        }
        return totalPrice;
    }

    @Override
    public void addCart(Long uid, Long prodId) {

    }

    @Override
    public List<Cart> getCarts(Long uid) {
        return cartRepository.findByUser_Uid(uid);
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId, Long uid) {
        cartRepository.deleteByCartIdAndUser_Uid(cartId, uid);
    }


}

