package com.example.rewrite.controller;

import com.example.rewrite.entity.Product;
import com.example.rewrite.repository.product.ProductRepository;
import com.example.rewrite.service.prod.ProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private ProdService prodService;


    @GetMapping("/search")
    public String searchProducts(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 서비스 계층을 통해 상품 검색
            List<Product> searchResults = prodService.searchProductsByTitle(keyword);
            model.addAttribute("products", searchResults);
            model.addAttribute("keyword", keyword);
        } else {
            // 검색어가 없으면 전체 상품 표시
            List<Product> allProducts = prodService.getAllProducts();
            model.addAttribute("products", allProducts);
        }

        return "prod/prodList"; // 기존 상품 목록 템플릿 사용
    }

}
