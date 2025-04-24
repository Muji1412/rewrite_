package com.example.rewrite.controller;

import com.example.rewrite.command.user.UserSessionDto;
import com.example.rewrite.entity.Wishlist;
import com.example.rewrite.service.wishlist.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * 찜하기 기능의 웹 요청을 처리하는 컨트롤러
 */
@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    /**
     * 사용자의 찜목록 페이지를 보여주는 메소드
     */
    @GetMapping
    public String getWishlistPage(Model model, HttpSession session) {
        // 로그인 확인
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        // 사용자의 찜목록 조회
        List<Wishlist> wishlistItems = wishlistService.getWishlistByUserId(user.getUid());
        model.addAttribute("wishlistItems", wishlistItems);

        return "wishlist/list";
    }

    /**
     * 찜하기/취소 기능을 처리하는 AJAX 요청 핸들러
     */
    @PostMapping("/toggle")
    @ResponseBody
    public ResponseEntity<?> toggleWishlist(@RequestParam Long prodId, HttpSession session) {
        // 로그인 확인
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        // 찜하기 상태 토글 (추가 또는 제거)
        boolean isAdded = wishlistService.toggleWishlist(user.getUid(), prodId);

        // 상품의 현재 찜 개수 조회
        int wishlistCount = wishlistService.getWishlistCount(prodId);

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("isWishlisted", isAdded);
        response.put("wishlistCount", wishlistCount);

        return ResponseEntity.ok(response);
    }
}
