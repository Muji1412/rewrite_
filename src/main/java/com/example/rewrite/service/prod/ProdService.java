package com.example.rewrite.service.prod;

import com.example.rewrite.command.ProductDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProdService {

    // 상품 목록 조회
    List<ProductDTO> getAllProducts();

    // 내 상품 목록 조회
    List<ProductDTO> getMyProducts(Long uid);

    // 상품 상세 조회
    ProductDTO getProductById(Long id);

    // 상품 등록
    ProductDTO registerProduct(ProductDTO productDTO);

    // 상품 수정
    ProductDTO updateProduct(ProductDTO productDTO);



}
