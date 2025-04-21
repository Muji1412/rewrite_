package com.example.rewrite.repository.review;

import com.example.rewrite.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT COALESCE(AVG(r.rating),0) FROM Review r where r.user.uid = :uid")
    double getAvgRating(@Param("uid") Long uid);
}
