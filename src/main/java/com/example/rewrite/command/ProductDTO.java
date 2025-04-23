package com.example.rewrite.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {

    private int prodId;
    private String categoryMax;
    private String categoryMin;
    private String title;
    private String description;
    private String pickupAddress;
    private LocalDateTime pickupDate;
    private String pickupStatus;
    private String prodStatus;
    private LocalDateTime regDate;
    private String price;
    private String videoUrl;
    private int viewcount;
    private Long uid;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String userNickname; // 사용자 닉네임
    private String userImgUrl;   // 사용자 프로필 이미지
}
