package com.example.rewrite.controller;

import com.example.rewrite.entity.Address;
import com.example.rewrite.service.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/detail") //주소지 페이지
    public String addressDetail(/*@RequestParam("addressId") String addressId,*/ Model model) {

        List<Address> list = addressService.getAddress();
        model.addAttribute("list", list);

        return "address/addressDetail";
    }

    @GetMapping("/reg") //주소지 등록 페이지
    public String addressReg(){
        return "address/addressReg";
    }

    @GetMapping("/delete") //주소지 삭제 페이지
    public String addressDelete(){

        return "redirect:/address/detail";
    }
}
