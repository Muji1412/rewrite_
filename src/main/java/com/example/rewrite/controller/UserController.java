package com.example.rewrite.controller;

import com.example.rewrite.command.UserVO;
import com.example.rewrite.repository.users.UsersRepository;
import com.example.rewrite.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }
    @GetMapping("/login")
    public String login() {return "user/login";}
    @GetMapping("/mypage")
    public String myPage(){
        return "user/mypage";
    }

}
