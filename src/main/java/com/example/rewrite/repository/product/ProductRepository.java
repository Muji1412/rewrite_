package com.example.rewrite.repository.product;

import com.example.rewrite.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // 기본 CRUD 메서드 제공
}
