package com.example.rewrite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (REST API에서는 보통 비활성화, 세션/쿠키 기반이라면 활성화 및 설정 필요)
                .authorizeHttpRequests(authorize -> authorize
                        // "/api/auth/signup" 경로는 인증 없이 누구나 접근 가능하도록 허용 ***매우 중요***
                        .antMatchers("/api/auth/signup", "/signup").permitAll()
                        // "/api/auth/login" 경로도 인증 없이 접근 가능하도록 허용
                        .antMatchers("/api/auth/login", "/login").permitAll()
                        // 정적 리소스(css, js, 이미지 등) 경로도 필요에 따라 허용
                        // .antMatchers("/", "/home", "/css/**", "/js/**", "/images/**").permitAll()
                        // 그 외 모든 요청은 인증된 사용자만 접근 가능하도록 설정
                        .anyRequest().authenticated()
                );
        // 필요에 따라 로그인 설정(formLogin, httpBasic), JWT 필터 등을 추가

        return http.build();
    }
}
