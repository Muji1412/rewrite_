package com.example.rewrite.service.user;

import com.example.rewrite.command.UserVO;
import com.example.rewrite.command.user.LoginRequestDto;
import com.example.rewrite.command.user.SignupRequestDto;
import com.example.rewrite.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.userdetails.User;


public interface UserService {

    Users signup(SignupRequestDto signupRequestDto);
    User loadUserByUsername(String id);
}
