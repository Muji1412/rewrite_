package com.example.rewrite.service.user;

import com.example.rewrite.command.UserVO;
import com.example.rewrite.command.user.ApiResponseDto;
import com.example.rewrite.command.user.FindIdRequestDto;
import com.example.rewrite.command.user.SignupRequestDto;
import com.example.rewrite.entity.Users;
import com.example.rewrite.repository.users.UsersRepository;
import com.example.rewrite.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service("UserService")
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션, 쓰기 작업 메소드에 @Transactional 추가
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Override
    @Transactional
    public Users signup(SignupRequestDto signupRequestDto) {

        if (usersRepository.existsById(signupRequestDto.getId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다");
        }
        if (usersRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다");
        }
        if (usersRepository.existsByNickname(signupRequestDto.getNickname())) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다");
        }
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPw());

        Users user = Users.builder()
                .id(signupRequestDto.getId())
                .pw(encodedPassword)
                .name(signupRequestDto.getName())
                .nickname(signupRequestDto.getNickname())
                .email(signupRequestDto.getEmail())
                .birth(signupRequestDto.getBirth())
                .phone(signupRequestDto.getPhone())
                .build();

        return usersRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String id) throws UsernameNotFoundException { //로그인에 쓰는 메서드
        Users user = usersRepository.findById(id) // 아이디가 있는지 체크함
                .orElseThrow(() -> new UsernameNotFoundException(id + "사용자를 찾을 수 없습니다.")); // 없으면 에러던짐

        List<GrantedAuthority> authorities = Collections //권한처리, GrantedAuthority = 사용자 권한 표현 인터페이스
                .singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));

        // 싱글톤리스트 쓰는 이유
        // 1. 스프링 시큐리티 User객체 = 무조건 Collection으로 권한을 받도록 만들어짐
        // 2. 대부분 하나의 권한만 주는데, 그럴때 효과적임
        // 3. 코드가 간결해지고, 효율성 좋고, 불변성임.
        // 하지만 여러 권한이 필요한 경우에는 ArrayList를 사용해야함 (한명이 여러개의 권한을 동적으로 가지는 경우)
        return new User(user.getId(), user.getPw(), authorities);
    }

    @Transactional(readOnly = true) // 조회 작업이므로 readOnly
    public void sendUserIdToEmail(FindIdRequestDto requestDto) {

        //사용자 존재 체크 + 유저 불러오기
        Users foundUser = usersRepository.findByNameAndPhoneAndEmail(
                requestDto.getName(),
                requestDto.getPhone(),
                requestDto.getEmail()
        ).orElseThrow(() -> new EntityNotFoundException("입력한 정보와 일치하는 유저가 없습니다22."));

        // 사용자가 존재하면 EmailService를 통해 아이디 발송
        mailService.sendUserIdByEmail(foundUser.getEmail(), foundUser.getId());
    }


    @Override
    public boolean checkUserByIdAndEmailAndPhoneAndPassword(FindIdRequestDto requestDto) {
        Optional<Users> user = usersRepository.findByIdAndNameAndPhoneAndEmail(
                requestDto.getId(),
                requestDto.getName(),
                requestDto.getPhone(),
                requestDto.getEmail()
        );
        return user.isPresent(); // 존재시 true 반환
    }

    @Override
    @Transactional
    public void sendUserPwdToEmail(FindIdRequestDto requestDto) {

        // 사용자 조회
        Users foundUser = usersRepository.findByIdAndNameAndPhoneAndEmail(
                requestDto.getId(),
                requestDto.getName(),
                requestDto.getPhone(),
                requestDto.getEmail()
            ).orElseThrow(() -> new EntityNotFoundException("해당 정보로 가입된 사용자를 찾을 수 없습니다."));

        String tempPassword = UUID.randomUUID().toString().substring(0, 10);
        String encodedPassword = passwordEncoder.encode(tempPassword);


        // 메일로 임시비밀번호 전송
        mailService.sendUserIdPasswordByEmail(foundUser.getEmail(), foundUser.getId(), tempPassword);

        foundUser.setPw(encodedPassword);
        log.info("비밀번호 변경 - 바뀐 비밀번호" + foundUser.getPw());
        usersRepository.save(foundUser);
    }

    @Override
    public Optional<Users> findById(String id) {
        return usersRepository.findById(id);
    }


    //회원정보 수정
    @Override
    @Transactional
    public void userModify(UserVO user) {

        user.setPw(passwordEncoder.encode(user.getPw()));

        usersRepository.userModify(user);
    }

    //회원탈퇴
    @Override
    @Transactional
    public void userDelete(Long uid) {
        String genId = "deleted_" + UUID.randomUUID().toString().substring(0, 8);
        usersRepository.userDelete(uid, genId);
    }
}
