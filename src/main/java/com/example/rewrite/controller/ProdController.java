package com.example.rewrite.controller;

import com.example.rewrite.command.ProductDTO;
import com.example.rewrite.repository.product.ProductRepository;
import com.example.rewrite.service.prod.ProdService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProdController {

    private final ProductRepository productRepository;
    private final ProdService prodService;

    public ProdController(ProductRepository productRepository, ProdService prodService) {
        this.productRepository = productRepository;
        this.prodService = prodService;
    }

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

    @GetMapping("/productReg")
    public String productReg() {
        return "prod/productReg";
    }

    @GetMapping("/prodDetail")
    public String prodDetail(@RequestParam int prodId, Model model){
        // 서비스를 통해 상품 상세 정보를 가져옴
        ProductDTO product = prodService.getProductById(prodId);
        // 모델에 상품 정보 추가
        model.addAttribute("product", product);
        return "prod/prodDetail";
    }

    @GetMapping("/prodList")
    public String prodList(Model model) {
        // 서비스를 통해 모든 상품 목록을 가져옴
        List<ProductDTO> products = prodService.getAllProducts();
        // 모델에 상품 목록 추가
        model.addAttribute("products", products);
        return "prod/prodList";
    }

    @PostMapping("/productReg")
    public String register(
            @ModelAttribute ProductDTO productDTO) {

        // 서비스 레이어를 통해 상품 등록
        prodService.registerProduct(productDTO);
        return "redirect:/prod/prodList";
    }
}
