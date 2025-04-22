package com.example.rewrite.controller;

import com.example.rewrite.command.UserVO;
import com.example.rewrite.command.user.UserSessionDto;
import com.example.rewrite.entity.Users;
import com.example.rewrite.repository.review.ReviewRepository;
import com.example.rewrite.repository.users.UsersRepository;
import com.example.rewrite.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }
    @GetMapping("/login")
    public String login() {return "user/login";}
    @GetMapping("/mypage")
    public String myPage(Model model) {

        return "user/mypage";
    }
    @GetMapping("/edit")
    public String userEdit(HttpSession session, Model model){
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        model.addAttribute("user", user);
        return "user/edit_profile";
    }

    @PostMapping("/modify")
    public String modify(HttpSession session, @RequestParam("nickname")String nickname,
                         @RequestParam("password")String password,
                         @RequestParam("eqpassword")String eqpassword){
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        UserVO vo = new UserVO();
        System.out.println("닉네임:  "+ user.getNickname() +"uid: "+ user.getUid() );
        vo.setNickname(nickname);
        vo.setPw(eqpassword);
        vo.setUid(user.getUid());

        if(!(password.equals(eqpassword))){

            return "redirect:/user/edit";
        }

        userService.userModify(vo);

        user.setNickname(nickname);
        session.setAttribute("users",user);
        return "/user/mypage";
    }
    @GetMapping("/cs_main")
    public String cs_main() {return "user/cs_main";}
    @GetMapping("/faq")
    public String faq() {return "user/faq";}

    @GetMapping("/idFind")
    public String idFind(){
        return "user/idFind";
    }
    @GetMapping("/pwFind")
    public String pwFind(){

        return "user/pwFind";
    }
}

