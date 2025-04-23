package com.example.rewrite.repository.product;

import com.example.rewrite.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 기본 CRUD 메서드 제공
    // 조회수 증가 쿼리
    @Modifying
    @Query("UPDATE Product p SET p.viewCount = COALESCE(p.viewCount, 0) + 1 WHERE p.prodId = :prodId")
    void incrementViewCount(@Param("prodId") Long prodId);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.user WHERE p.prodId = :prodId")
    Optional<Product> findProductWithUserById(@Param("prodId") Long prodId);

    List<Product> prodId(Long prodId);

    List<Product> findProductsByUserUid(Long uid);
}
