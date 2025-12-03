package com.server.smzshop.users.auth.presentation;

import com.server.smzshop.users.auth.dto.AuthTokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class LoginResponse {
    //TODO 유저 아이디
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;

    public static LoginResponse from(AuthTokenDto authToken){
        return LoginResponse.builder()
                .accessToken(authToken.getAccessToken())
                .refreshToken(authToken.getRefreshToken())
                .tokenType(authToken.getTokenType())
                .expiresIn(authToken.getAccessTokenExpiresIn())
                .build();
    }

}
