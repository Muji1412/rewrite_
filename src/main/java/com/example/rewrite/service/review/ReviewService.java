package com.example.rewrite.service.review;

import com.example.rewrite.entity.Review;

import java.util.List;

public interface ReviewService {

    List<Review> getReviewsByUid(Long uid);

}
