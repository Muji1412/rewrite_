package com.example.rewrite.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductVO {

    private int prodId;
    private String categoryMax;
    private String categoryMin;
    private String title;
    private String description;
    private String pickupAddress;
    private java.time.LocalDateTime pickupDate;
    private String pickupStatus;
    private String prodStatus;
    private java.time.LocalDateTime regDate;
    private String price;
    private String videoUrl;
    private int viewcount;
    private Long uid;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
}
