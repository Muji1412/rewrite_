package com.example.rewrite.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cart")
@AllArgsConstructor
@ToString
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "prod_id")
    private Product product;

    @Column(name = "added_at")
    @CreationTimestamp
    private LocalDateTime addedAt;


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude // 양방향 연관관계 ToString 무한 루프 방지
    private List<Cart> cartItems = new ArrayList<>();

}
