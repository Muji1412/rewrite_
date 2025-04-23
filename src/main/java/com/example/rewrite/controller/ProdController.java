package com.example.rewrite.controller;

import com.example.rewrite.command.ProductDTO;
import com.example.rewrite.command.user.UserSessionDto;
import com.example.rewrite.entity.Cart;
import com.example.rewrite.repository.product.ProductRepository;
import com.example.rewrite.service.cart.CartService;
import com.example.rewrite.service.prod.ProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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

    @GetMapping("/orderPay")
    public String orderPay() {
        return "prod/orderPay";
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
    public String prodDetail(@RequestParam Long prodId, Model model){
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

    @GetMapping("/myProdList")
    public String myProdList(HttpSession session, Model model){
        UserSessionDto user = (UserSessionDto)session.getAttribute("user");

        model.addAttribute("products", prodService.getMyProducts(user.getUid()));

        return  "prod/prodList";
    }

    @PostMapping("/productReg")
    public String register(
            @ModelAttribute ProductDTO productDTO,
            RedirectAttributes redirectAttributes) {

        // 서비스 레이어를 통해 상품 등록
        prodService.registerProduct(productDTO);

        // 성공 메시지 추가
        redirectAttributes.addFlashAttribute("success", "상품이 성공적으로 등록되었습니다.");
        System.out.println("Flash 속성 설정됨: success = 상품이 성공적으로 등록되었습니다.");

        return "redirect:/prod/prodList";
    }

    // 상품 수정 페이지 이동
    @GetMapping("/productUpdate")
    public String productUpdate(@RequestParam Long prodId, Model model) {
        ProductDTO product = prodService.getProductById(prodId);
        model.addAttribute("product", product);
        return "prod/productReg";  // 등록 페이지를 재사용
    }

    // 상품 수정 처리
    @PostMapping("/productUpdate")
    public String updateProduct(@ModelAttribute ProductDTO productDTO) {
        // 서비스 레이어를 통해 상품 수정
        prodService.updateProduct(productDTO);
        return "redirect:/prod/prodDetail?prodId=" + productDTO.getProdId();
    }

}
