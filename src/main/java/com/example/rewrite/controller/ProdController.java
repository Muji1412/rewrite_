package com.example.rewrite.controller;

import com.example.rewrite.entity.Cart;
import com.example.rewrite.service.cart.CartService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.rewrite.entity.Product;
import com.example.rewrite.repository.product.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/prod")
public class ProdController {

    @Autowired
    private CartService cartService;

    private final ProductRepository productRepository;

    public ProdController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

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

    @GetMapping("/prodDetail")
    public String prodDetail(){ return "prod/prodDetail"; }
    @GetMapping("/prodList")
    public String prodList(){ return "prod/prodList"; }
    @GetMapping("/orderList")
    public String orderList(){ return "prod/orderList"; }

    @GetMapping("/productReg")
    public String reg(){
        return "prod/productReg";
    }

    @PostMapping("/productReg")
    public String register(
            @RequestParam String title,
            @RequestParam String category_max,
            @RequestParam String category_min,
            @RequestParam String price,
            @RequestParam String description,
            @RequestParam(required = false) String img_1,
            @RequestParam(required = false) String img_2,
            @RequestParam(required = false) String img_3,
            @RequestParam(required = false) String img_4,
            @RequestParam(required = false) String video_url
    ) {
        Product product = Product.builder()
                .title(title)
                .categoryMax(category_max)
                .categoryMin(category_min)
                .price(price)
                .description(description)
                .img1(img_1)
                .img2(img_2)
                .img3(img_3)
                .img4(img_4)
                .videoUrl(video_url)
                .regDate(LocalDateTime.now())
                .build();

        productRepository.save(product);
        return "redirect:/prod/prodList";
    }
}
