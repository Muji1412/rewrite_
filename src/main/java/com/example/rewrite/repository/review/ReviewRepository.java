package com.example.rewrite.repository.review;

import com.example.rewrite.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByUserUid(Long uid);

    // 로그인한 유저(uid)가 해당 상품(prodId)에 대해 리뷰를 썼는지 확인
    boolean existsByUserUidAndProductProdId(Long uid, Long prodId);

}
