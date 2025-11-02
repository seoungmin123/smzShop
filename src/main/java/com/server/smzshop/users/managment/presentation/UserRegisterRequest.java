package com.server.smzshop.users.managment.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterRequest (

        @NotBlank(message = "아이디는 필수 입니다.")
        @Pattern(regexp = "^[a-z0-9]{4,10}$" , message = "아이디는 영문 소문자와 숫자 4-10자리 입니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수 입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,15}$", message = "비밀번호는 영문 대소문자와 숫자를 포함한 8-15자리입니다.")
        String password,

        @NotBlank(message = "이름은 필수 입니다.")
        String name
){
}
