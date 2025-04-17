package com.example.rewrite.service.user;

import com.example.rewrite.command.UserVO;
import com.example.rewrite.command.user.SignupRequestDto;
import com.example.rewrite.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


public interface UserService {

    Users signup(SignupRequestDto signupRequestDto);
}
