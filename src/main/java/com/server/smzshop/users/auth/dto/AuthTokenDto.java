package com.server.smzshop.users.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenDto {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long accessTokenExpiresIn; //초 단위

    public static AuthTokenDto of(String accessToken, String refreshToken, Long accessTokenExpireTime) {
        return AuthTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .accessTokenExpiresIn(accessTokenExpireTime/1000)
                .build();
    }
}
