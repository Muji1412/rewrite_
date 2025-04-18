package com.example.rewrite.controller;

import com.example.rewrite.entity.Address;
import com.example.rewrite.repository.address.AddressRepository;
import com.example.rewrite.service.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        return "address/addressWrite";
    }

    @PostMapping("/write")
    public String addressWrite(Address address,
                               @RequestParam("postcode")String postcode,
                               @RequestParam("addr")String addr,
                               @RequestParam("detailAddress")String addressDetail,
                               @RequestParam("phone1")String phone1,
                               @RequestParam("phone2")String phone2,
                               @RequestParam("phone3")String phone3
                               ){
        address.setAddress(postcode+"/"+addr+"/"+addressDetail);
        address.setPhoneNum(phone1 + phone2 + phone3);
        System.out.println(address.toString());
        addressService.addressWrite(address);

        return "redirect:/address/detail";
    }

    @GetMapping("/edit") //주소지 수정 페이지
    public String addressEdit(@RequestParam("addressId")String addressId, Model model){
        System.out.println("addressId = " + addressId);
        return "address/addressEdit";
    }

    @GetMapping("/delete") //주소지 삭제 페이지
    public String addressDelete(){

        return "redirect:/address/detail";
    }
}
