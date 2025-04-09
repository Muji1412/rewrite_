package com.example.rewrite.command;

import lombok.Data;

@Data
public class UserVO {
    private String key;
    private String id;
    private String pw;
    private String name;
    private String birth;
    private String imgUrl;
    private String createdAt;
    private String phone;
    private String role;
}
