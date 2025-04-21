package com.example.rewrite.service.user;

import com.example.rewrite.command.UserVO;
import com.example.rewrite.command.user.FindIdRequestDto;
import com.example.rewrite.command.user.LoginRequestDto;
import com.example.rewrite.command.user.SignupRequestDto;
import com.example.rewrite.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;


public interface UserService {

    Users signup(SignupRequestDto signupRequestDto);
    User loadUserByUsername(String id);
    void userModify(UserVO user);
    void sendUserIdToEmail(FindIdRequestDto requestDto);
    boolean checkUserByNameAndPhoneAndEmail(FindIdRequestDto requestDto);
}
