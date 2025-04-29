package com.example.rewrite.controller;


import com.example.rewrite.command.UserVO;
import com.example.rewrite.entity.Review;
import com.example.rewrite.service.review.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/review")
    public String review() {
        return "review/review";
    }

    @GetMapping("/reviewList")
    public String reviewList(HttpSession session, Model model, ReviewService reviewService) {
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        Long uid = loginUser.getUid();
        List<Review> reviews = reviewService.getReviewsByUid(uid);
        model.addAttribute("reviews", reviews);
        return "review/reviewList";
    }
}