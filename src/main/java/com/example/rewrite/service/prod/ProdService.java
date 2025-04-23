package com.example.rewrite.service.prod;

import com.example.rewrite.command.ProductDTO;

import java.util.List;

public interface ProdService {

    // 상품 목록 조회
    List<ProductDTO> getAllProducts();

    // 상품 상세 조회
    ProductDTO getProductById(int id);

    // 상품 등록
    ProductDTO registerProduct(ProductDTO productDTO);

}
