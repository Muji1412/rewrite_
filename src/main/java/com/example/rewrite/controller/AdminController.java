package com.example.rewrite.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/modifyUsers")
    public String modifyUsers() {
        return "admin/modifyUsers";
    }
    @GetMapping("/noticeWrite") public String noticeWrite(Model model) {return "notice/noticeWrite";}
}
