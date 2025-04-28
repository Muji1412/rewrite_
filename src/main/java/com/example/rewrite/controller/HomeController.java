package com.example.rewrite.controller; // 패키지 경로 확인!

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
        public String home() {
            return "main";
    }
    @GetMapping("/home")
    public String home2() {
        return "home";
    }
}