package com.server.smzshop.common.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("비밀번호 암호화")
    void pwdEnc(){
        String pwd = "smsm01611";
        String encodedPwd = passwordEncoder.encode(pwd); // 암호화
        System.out.println(encodedPwd);

//        {bcrypt}$2a$10$bLHn5Zy5l.g/GHq5SuMjlep3O98YpnrCsjhCbLZBHeAoji9GSiniG
    }

    @Test
    @DisplayName("비밀번호 검증")
    void pwdMath(){
        String encodedPwd ="{bcrypt}$2a$10$bLHn5Zy5l.g/GHq5SuMjlep3O98YpnrCsjhCbLZBHeAoji9GSiniG";
        String newPwd = "smsm01611";
//        Boolean matched = passwordEncoder.matches(newPwd ,encodedPwd);
//        System.out.println(matched);

        Assertions.assertThat(passwordEncoder.matches(newPwd ,encodedPwd)).isTrue();
    }

}