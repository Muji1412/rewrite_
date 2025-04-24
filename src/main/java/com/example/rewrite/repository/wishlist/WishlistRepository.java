package com.example.rewrite.repository.wishlist;

import com.example.rewrite.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    /**
     * 특정 사용자의 모든 찜 목록을 조회하는 메소드
     *
     * @param uid 조회할 사용자의 UID
     * @return 사용자의 찜 항목 목록
     */
    List<Wishlist> findByUserUid(Long uid);

    /**
     * 특정 사용자가 특정 상품을 찜했는지 확인하는 메소드
     *
     * @param uid 확인할 사용자의 UID
     * @param prodId 확인할 상품의 PROD_ID
     * @return 찜했으면 true, 아니면 false
     */
    boolean existsByUserUidAndProductProdId(Long uid, Long prodId);

    /**
     * 특정 사용자의 특정 상품 찜을 삭제하는 메소드
     *
     * @param uid 찜을 삭제할 사용자의 UID
     * @param prodId 찜을 삭제할 상품의 PROD_ID
     */
    void deleteByUserUidAndProductProdId(Long uid, Long prodId);

    /**
     * 특정 상품이 받은 총 찜 개수를 조회하는 메소드
     *
     * @param prodId 찜 개수를 확인할 상품의 PROD_ID
     * @return 해당 상품의 찜 개수
     */
    @Query("SELECT COUNT(w) FROM Wishlist w WHERE w.product.prodId = :prodId")
    int countByProductProdId(Long prodId);
}
