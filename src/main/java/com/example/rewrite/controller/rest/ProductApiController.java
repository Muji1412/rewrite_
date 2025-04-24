package com.example.rewrite.controller.rest;

import com.example.rewrite.command.ProductDTO;
import com.example.rewrite.command.user.UserSessionDto;
import com.example.rewrite.service.prod.ProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProdService productService;

    @Autowired
    public ProductApiController(ProdService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "latest") String sortBy) {
        List<ProductDTO> products = productService.getAllProducts(sortBy);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // DTO에 현재 사용자 ID 설정
        productDTO.setUid(user.getUid());

        // 상품 등록
        ProductDTO createdProduct = productService.registerProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // 추가로 필요한 PUT, DELETE 메소드도 구현 필요
}
