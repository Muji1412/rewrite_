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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }
    @GetMapping("/login")
    public String login() {return "user/login";}

    @GetMapping("/mypage")
    public String myPage(HttpSession session ,Model model) {
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if(user == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("nickname", user.getNickname());
        model.addAttribute("user", userService.getProfile(user.getUid()));
        model.addAttribute("sellprod", userService.getSellProd(user.getUid()));
        model.addAttribute("sellcount", userService.sellCount(user.getUid()));
        return "user/mypage";
    }
    @GetMapping("/edit")
    public String userEdit(HttpSession session, Model model){
        UserSessionDto user = (UserSessionDto)session.getAttribute("user");
        if(user == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", user);
        return "user/edit_profile";
    }

    @PostMapping("/modify")
    public String modify(HttpSession session,
                         @RequestParam(value = "imgUrl", required = false) String imgUrl,
//                         @RequestParam("pw") String pw,
                         @RequestParam("nickname") String nickname,
                         @RequestParam("password") String password,
                         @RequestParam("eqpassword") String eqpassword,
                         RedirectAttributes redirectAttributes) {

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if(user == null) {
            return "redirect:/user/login";
        }

//        if(!pw.equals(userService.getPassword(user.getUid()))){
//            redirectAttributes.addFlashAttribute("message", "현재 비밀번호가 일치하지 않습니다.");
//            return "redirect:/user/edit";
//        }
        UserVO vo = new UserVO();
        vo.setNickname(nickname);
        vo.setPw(eqpassword);
        vo.setUid(user.getUid());

        if(!(password.equals(eqpassword))){
            redirectAttributes.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");
            return "redirect:/user/edit";
        }

        // 이미지 URL이 제공된 경우에만 설정
        if (imgUrl != null && !imgUrl.isEmpty()) {
            vo.setImgUrl(imgUrl);
        }


        try {
            userService.userModify(vo);

            // 세션 정보도 업데이트
            user.setNickname(nickname);
            if (imgUrl != null && !imgUrl.isEmpty()) {
                user.setImgUrl(imgUrl);
            }
            session.setAttribute("user", user);

            redirectAttributes.addFlashAttribute("message", "회원정보가 수정되었습니다.");
            return "redirect:/user/mypage";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "회원정보 수정 중 오류가 발생했습니다.");
            return "redirect:/user/edit";
        }

    }

    @PostMapping("/delete")    //회원 탈퇴
    public String delete(HttpSession session){

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if(user == null) {
            return "redirect:/user/login";
        }
        userService.userDelete(user.getUid()); 
        return "redirect:/logout"; //스프링 시큐리티 로그아웃 
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
    public String pwFind(){return "user/pwFind";}

}

