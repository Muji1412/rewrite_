package com.example.rewrite.controller; // 패키지 경로 확인!

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // <-- 이 어노테이션이 있는지 확인!
public class HomeController {

    @GetMapping("/") // <-- 루트 경로 ("/") 매핑 확인!
    public String home() {
        return "home"; // <-- "home"을 반환하는지 확인! (그러면 templates/home.html을 찾음)
    }
}