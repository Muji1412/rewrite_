package com.example.rewrite.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp; // Hibernate 사용 시


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "id", nullable = false, unique = true, length = 50)
    private String id;

    @Column(name = "pw", nullable = false, length = 255)
    private String pw;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "nickname", length = 30)
    private String nickname;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "birth") // DATE 타입은 LocalDate와 매핑
    private LocalDate birth;


    @Column(name = "img_url", length = 512)
    @Builder.Default
    private String imgUrl = "https://storage.googleapis.com/rewrite_project/profile_img/user_default.png";

    @CreationTimestamp // 엔티티 생성 시 자동으로 현재 시간 저장
    @Column(name = "reg_date", nullable = false, updatable = false)
    private LocalDateTime regDate;

    @Column(name = "phone", length = 25)
    private String phone;

    @Column(name = "role", nullable = false, length = 20)
    @Builder.Default // **** 이 어노테이션 추가 ****
    private String role = "user"; // 자바 객체 생성 시 기본값

    // --- Cascade 설정 추가 ---

    /**
     * 사용자가 등록한 상품 목록.
     * mappedBy: Product 엔티티 내부에 있는 Users 타입 필드 이름 (아마도 "user")
     * cascade = CascadeType.REMOVE: Users 엔티티가 삭제될 때 연관된 Product 엔티티도 삭제됨
     * orphanRemoval = true: User의 products 리스트에서 Product 객체를 제거하면 DB에서도 해당 Product 삭제됨
     * fetch = FetchType.LAZY: products 리스트를 실제로 사용할 때 DB에서 로딩 (성능 최적화)
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude // Lombok @ToString 사용 시 순환 참조 방지
    private List<Product> products = new ArrayList<>();

    /**
     * 사용자가 작성한 리뷰 목록.
     * mappedBy: Review 엔티티 내부에 있는 Users 타입 필드 이름 (아마도 "user")
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();

    /**
     * 사용자의 장바구니 항목 목록.
     * mappedBy: Cart 엔티티 내부에 있는 Users 타입 필드 이름 (아마도 "user")
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Cart> cartItems = new ArrayList<>(); // 필드 이름 예시

}