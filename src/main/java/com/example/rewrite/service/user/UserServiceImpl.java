package com.example.rewrite.service.user;

import com.example.rewrite.command.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVO loginForm(UserVO vo) {

        return userMapper.loginForm(vo);
    }

    @Override
    public int register(UserVO vo) {

        return userMapper.registerUser(vo);
    }
}
