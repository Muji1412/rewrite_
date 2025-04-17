package com.example.rewrite.service.user;

import com.example.rewrite.command.UserVO;
import com.example.rewrite.command.user.SignupRequestDto;
import com.example.rewrite.entity.Users;
import com.example.rewrite.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("UserService")
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션, 쓰기 작업 메소드에 @Transactional 추가
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
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
}
