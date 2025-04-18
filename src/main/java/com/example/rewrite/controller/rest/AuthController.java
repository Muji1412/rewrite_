package com.example.rewrite.controller.rest;

import com.example.rewrite.command.user.SignupRequestDto;
import com.example.rewrite.entity.Users; // 선택사항: 반환값으로 사용자 정보 일부를 포함하고 싶을 때
import com.example.rewrite.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {

        try {
            Users registeredUser = userService.signup(signupRequestDto);


            // 회원가입 성공시, 성공 메세지를 body에 담아주면 json형식으로 보내줌.
            Map<String, Object> successBody = new HashMap<>();
            successBody.put("message", "회원가입을 환영합니다. " + registeredUser.getName() +"님!");

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(successBody);

        } catch (IllegalArgumentException e) { //실패케이스 - @Valid로 지정한 오류들(중복 등)

            // 회원가입 실패처리
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorBody);

        } catch (Exception e) { //실패케이스 - 서버오류
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorBody);
        }
    }
}