package com.example.rewrite.repository.review;

import com.example.rewrite.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserUid(Long uid);

}
