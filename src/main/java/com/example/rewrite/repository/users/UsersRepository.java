package com.example.rewrite.repository.users;

import com.example.rewrite.command.UserVO;
import com.example.rewrite.entity.Product;
import com.example.rewrite.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    // 회원가입 관련 메서드
    Optional<Users> findById(String id);
    boolean existsById(String id);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByNameAndPhoneAndEmail(String name, String phone, String email);
    Optional<Users> findByIdAndNameAndPhoneAndEmail(String id, String name, String phone, String email);



    @Modifying
    @Transactional
    @Query("UPDATE Users u " +
            "Set u.nickname = :#{#user.nickname}, " +
            "u.pw = :#{#user.pw}," +
            " u.imgUrl = :#{#user.imgUrl} " +
            "where u.uid = :#{#user.uid}")
    void userModify(@Param("user")UserVO users);

    @Modifying
    @Transactional
    @Query("UPDATE Users u " +
            "set u.email = '탈퇴한회원'," +
            "u.id=:uuid," +
            "u.name='탈퇴한 회원'," +
            "u.nickname='탈퇴한 회원'," +
            "u.phone='탈퇴한회원'," +
            "u.pw=:uuid," +
            "u.role='탈퇴한회원'" +
            "where u.uid = :uid")
    void userDelete(@Param("uid") Long uid,
                    @Param("uuid")String uuid); //회원 탈퇴

    @Query("SELECT COUNT(p)" +
            "FROM Product p WHERE p.user.uid = :uid ")
    String sellCount(@Param("uid")Long uid);

    Users findUserByUid(Long uid);

    @Query("SELECT p FROM Product p where p.user.uid = :uid")
    List<Product> getSellProd(Long uid);
}
