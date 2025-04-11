package com.example.rewrite.service.user;

import com.example.rewrite.command.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


public interface UserService {

    UserVO loginForm(UserVO vo);
}
