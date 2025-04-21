package com.example.rewrite.controller;

import com.example.rewrite.entity.Cart;
import com.example.rewrite.service.cart.CartService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProdController {

    @Autowired
    private CartService cartService;

    @GetMapping("/checkout")
    public String checkout() {
        return "prod/checkout";
    }
    @GetMapping("/cart")
    public String getCart(Model model,@RequestParam("uid") Long uid) {
        List<Cart> cartList = cartService.getUserCart(uid);
        model.addAttribute("cartList", cartList);
        System.out.println(cartList);
        return "prod/cart";
    }

    @GetMapping("/orderDetail")
    public String orderDetail() {
        return "prod/orderDetail";
    }
    @GetMapping("/reg")
    public String reg(){
        return "prod/productReg";
    }
}
