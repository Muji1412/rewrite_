package com.example.rewrite.controller;

import com.example.rewrite.command.user.UserSessionDto;
import com.example.rewrite.entity.Cart;
import com.example.rewrite.entity.Product;
import com.example.rewrite.repository.product.ProductRepository;
import com.example.rewrite.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProdController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProdService prodService;

    private final ProductRepository productRepository;

    public ProdController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "prod/checkout";
    }
    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {
        UserSessionDto user = (UserSessionDto)session.getAttribute("user");
        if(user == null) {
            return "redirect:/user/login";
        }
        Long uid = user.getUid();
        System.out.println(uid);
        List<Cart> cartList = cartService.getCarts(uid);
        int totalPrice = cartService.calculateTotalPrice(cartList);

        model.addAttribute("cartList", cartList);
        for (Cart cart : cartList) {
            System.out.println("cart tostring");
            System.out.println(cart.toString());
            System.out.println("cart getUser tostring");
            System.out.println(cart.getUser().toString());
            System.out.println("cart getProduct tostring");
            System.out.println(cart.getProduct().toString());

        }
        model.addAttribute("totalPrice", totalPrice);

        return "prod/cart";
    }

    @GetMapping("/orderDetail")
    public String orderDetail() {
        return "prod/orderDetail";
    }


    @GetMapping("/productReg")
    public String reg(){
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
