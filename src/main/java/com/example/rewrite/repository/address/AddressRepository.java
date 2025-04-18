package com.example.rewrite.repository.address;

import com.example.rewrite.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {

    //주소 삭제 체크
    @Modifying
    @Transactional
    @Query("UPDATE Address a " +
            "SET a.delChk = 'C'" +
            "WHERE a.addressId = :addressId")
    void deleteAddress(Long addressId);

    //기본 주소지 체크
    @Modifying
    @Transactional
    @Query("UPDATE Address a " +
            "SET a.isDefault = CASE " +
            "    WHEN a.addressId = :addressId THEN 'C' " +
            "    WHEN a.uid = :uid AND a.addressId != :addressId THEN 'N' " +
            "    ELSE a.isDefault " +
            "END " +
            "WHERE a.uid = :uid")
    void checkDefault(Long addressId, String uid);

    //주소지 조회
    @Query("SELECT a FROM Address a " +
            "WHERE a.uid = :uid " +
            "AND a.delChk !='C'" +
            "OR a.delChk IS NULL " + //delChk가 C 가 아닌 것만 조회 //C일경우 삭제상태인것
            "ORDER BY a.isDefault")
    List<Address> getAddress(@Param("uid") String uid);

    //주소업데이트
    Address findByAddressId(Long addressId);
}
