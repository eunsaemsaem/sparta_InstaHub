package com.sparta.instahub.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 비밀번호 암호화하는 config - 강의에서 Bean으로 사용했던 기능을 분리시킨겁니다
 */
@Configuration
public class PasswordConfig { // passwordConfigg

    @Bean
    public PasswordEncoder passwordEncoder() { // passwordEncoder로 빈 등록
        //BCrypt: 해시함수: 비밀번호 암호화 -> BCryptPasswordEncoder 암호화
        return new BCryptPasswordEncoder(); // PasswordEncoder는 인터페이스로 주입받음 -> BCryptPasswordEncoder구현체
    }
}