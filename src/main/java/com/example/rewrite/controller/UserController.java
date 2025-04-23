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

    @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }
    @GetMapping("/login")
    public String login() {return "user/login";}

    @GetMapping("/mypage")
    public String myPage(HttpSession session ,Model model) {
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        model.addAttribute("nickname", user.getNickname());
        model.addAttribute("user", userService.getProfile(user.getUid()));
        model.addAttribute("sellprod", userService.getSellProd(user.getUid()));
        model.addAttribute("sellcount", userService.sellCount(user.getUid()));
        return "user/mypage";
    }
    @GetMapping("/edit")
    public String userEdit(HttpSession session, Model model){
        UserSessionDto user = (UserSessionDto)session.getAttribute("user");
        model.addAttribute("user", user);
        return "user/edit_profile";
    }

    @PostMapping("/modify")
    public String modify(HttpSession session,
                         @RequestParam(value = "imgUrl", required = false) String imgUrl,
                         @RequestParam("nickname") String nickname,
                         @RequestParam("password") String password,
                         @RequestParam("eqpassword") String eqpassword,
                         RedirectAttributes redirectAttributes) {

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
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

//        try {
//            if (!profileImage.isEmpty()) {
//                // 현재 프로젝트의 resources/static 경로 가져오기
//                String projectPath = new File("").getAbsolutePath();
//                String uploadDir = projectPath + "/src/main/resources/static/img/uploads";
//
//                File uploadPath = new File(uploadDir);
//                if (!uploadPath.exists()) {
//                    uploadPath.mkdirs();
//                }
//
//                String fileName = profileImage.getOriginalFilename();
//                String fileExtension = fileName.substring(fileName.lastIndexOf("."));
//                String savedFilename = UUID.randomUUID().toString() + fileExtension;
//
//                // 파일 저장
//                File dest = new File(uploadPath + File.separator + savedFilename);
//                profileImage.transferTo(dest);
//
//                // 브라우저에서 접근할 수 있는 상대 경로
//                String imageUrl = "/img/uploads/" + savedFilename;
//                vo.setImgUrl(imageUrl);
//            }
//
//            userService.userModify(vo);
//            user.setNickname(nickname);
//            if (vo.getImgUrl() != null) {
//                user.setImgUrl(vo.getImgUrl());
//            }
//            session.setAttribute("user", user);
//
//            redirectAttributes.addFlashAttribute("message", "회원정보가 수정되었습니다.");
//            return "redirect:/user/mypage";
//
//        } catch (Exception e) {
//            log.error("파일 업로드 중 오류 발생: {}", e.getMessage(), e);
//            redirectAttributes.addFlashAttribute("message", "회원정보 수정 중 오류가 발생했습니다.");
//            return "redirect:/user/edit";
//        }
    }

    @GetMapping("/delete")    //회원 탈퇴
    public String delete(HttpSession session){
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        userService.userDelete(user.getUid());
        session.invalidate();
        return "redirect:/";
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

