package com.example.rewrite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prod")
public class ProdController {

    @GetMapping("/checkout")
    public String checkout() {
        return "prod/checkout";
    }
    @GetMapping("/cart")
    public String cart() {
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
