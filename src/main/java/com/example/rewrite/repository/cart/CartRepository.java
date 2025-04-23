package com.example.rewrite.repository.cart;

import com.example.rewrite.command.user.UserSessionDto;
import com.example.rewrite.entity.Cart;
import com.example.rewrite.entity.Product;
import com.example.rewrite.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser_Uid(Long uid);

    void deleteByCartIdAndUser_Uid(Long cartId, Long uid);

}
