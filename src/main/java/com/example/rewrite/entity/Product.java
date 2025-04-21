package com.example.rewrite.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PROD_ID",nullable = false ,length = 255)
    private int prodId;

    @Column(name = "PRICE")
    private String price;

    @Column(name="TITLE", length = 255)
    private String title;

    @Column(name="DESCRIPTION" , length = 1000)
    private String description;

    @Column(name="PROD_STATUS",length = 50)
    private String prodStatus;

    @Column(name = "VIEWCOUNT")
    private Integer viewCount;

    @Column(name = "REG_DATE")
    private LocalDateTime regDate;

    @Column(name = "PICKUP_ADDRESS", length = 100)
    private String pickupAddress;

    @Column(name = "PICKUP_DATE")
    private LocalDateTime pickupDate;

    @Column(name = "PICKUP_STATUS", length = 50)
    private String pickupStatus;

    @Column(name = "VIDEO_URL", length = 200)
    private String videoUrl;

    @Column(name = "CATEGORY_ID") // 역시 백틱 처리
    private Long categoryId;

    @Column(name = "UID", length = 20)
    private String uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UID", referencedColumnName = "uid")
    private Users user;




}
