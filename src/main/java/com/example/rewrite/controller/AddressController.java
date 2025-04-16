package com.example.rewrite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/address")
public class AddressController {

    @RequestMapping("/detail")
    public String addressDetail(){
        return "address/addressDetail";
    }
}
