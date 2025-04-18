package com.example.rewrite.controller.rest;

import com.example.rewrite.command.user.LoginRequestDto;
import com.example.rewrite.command.user.SignupRequestDto;
import com.example.rewrite.command.user.UserSessionDto;
import com.example.rewrite.entity.Users; // 선택사항: 반환값으로 사용자 정보 일부를 포함하고 싶을 때
import com.example.rewrite.repository.users.UsersRepository;
import com.example.rewrite.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, HttpServletRequest request) {

        try {
            Users registeredUser = userService.signup(signupRequestDto);

            // 성공시 세션에 로그인데이터 담아서 넣어줌
            HttpSession session = request.getSession();
            UserSessionDto userSessionDto = UserSessionDto.fromEntity(registeredUser); //유저 엔티티 -> 유저세션DTO로 변경
            session.setAttribute("user", userSessionDto); //

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
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {

        try {
            //auth매니저한테 인증요청 보내봄
            Authentication authentication = authenticationManager.authenticate(
                    // 지금 받은 id/pw로 토큰 생성함
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getId(), loginRequestDto.getPw())
            );

            // 인증 성공 -> 시큐리티 컨텍스트홀더에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 세션 생성하기
            HttpSession session = request.getSession(true);

            // 응답보내줄 사용자 정보
            // 인증된 사용자 정보 가져오기
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 유저디테일을 users 엔티티로 변환 (디테일서비스에서는 role 외에는 정보가 딸림)

            // findByUsername(username) 은 Optional<Users> 를 반환
            Optional<Users> userOptional = usersRepository.findById(userDetails.getUsername());

            // Users 엔티티에 넣어줌.
            Users loggedInUser = usersRepository.findById(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("인증 후 사용자 정보를 찾을 수 없음")); // 예외 처리 필요함

            UserSessionDto userSessionDto = UserSessionDto.fromEntity(loggedInUser);
            session.setAttribute("user", userSessionDto);

            // 성공 응답 반환
            return ResponseEntity.ok(userSessionDto);

        }catch (BadCredentialsException e) { // 인증 실패 (비번 틀림 등)
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("message", e.getMessage() + "아이디 혹은 비밀번호가 잘못되었습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(errorBody);

        }catch (Exception e) { // 기타 서버 에러 발생시
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("message", e.getMessage() + " 기타 오류가 발생하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorBody);
        }
    }
}