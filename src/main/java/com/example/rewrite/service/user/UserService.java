package com.example.rewrite.service.user;

import com.example.rewrite.command.UserVO;
import com.example.rewrite.command.user.FindIdRequestDto;
import com.example.rewrite.command.user.SignupRequestDto;
import com.example.rewrite.entity.Users;
import org.springframework.security.core.userdetails.User;


public interface UserService {

    Users signup(SignupRequestDto signupRequestDto);
    User loadUserByUsername(String id);
    void userModify(UserVO user);
    void userDelete(Long uid);
    void sendUserIdToEmail(FindIdRequestDto requestDto);
    boolean checkUserByIdAndEmailAndPhoneAndPassword(FindIdRequestDto requestDto);
    void sendUserPwdToEmail(FindIdRequestDto requestDto);
}
