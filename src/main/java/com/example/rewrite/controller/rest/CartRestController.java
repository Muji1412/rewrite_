package com.example.rewrite.controller.rest;

import com.example.rewrite.command.user.UserSessionDto;
import com.example.rewrite.service.cart.CartService;
import org.hibernate.id.GUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    private CartService cartService;

    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteCart(@PathVariable Long cartId, HttpSession session) {
        try {
            UserSessionDto user = (UserSessionDto) session.getAttribute("user");
            System.out.println("삭제 요청: cartId=" + cartId + ", uid=" + user.getUid());

            cartService.deleteCart(cartId, user.getUid());

            return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("삭제 오류: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("삭제 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

