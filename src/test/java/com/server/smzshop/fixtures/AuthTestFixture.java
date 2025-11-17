package com.server.smzshop.fixtures;

import com.server.smzshop.users.auth.domain.RefreshToken;
import com.server.smzshop.users.auth.dto.AuthTokenDto;
import com.server.smzshop.users.auth.presentation.LoginRequest;

import java.time.LocalDateTime;

public class AuthTestFixture {

    // LoginRequest 객체
    public static LoginRequest createLoginRequest(String userId, String password) {
        return new LoginRequest(userId, password);
    }

    // AuthTokenDto 객체
    public static AuthTokenDto createAuthTokenDto() {
        return AuthTokenDto.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .accessTokenExpiresIn(3600L)
                .build();
    }

    //RefreshToken 만료 객체
    public static RefreshToken createExpiredRefreshToken(String refreshToken, String userId) {
       return RefreshToken.builder()
                .token(refreshToken)
                .userId(userId)
                .expiresAt(LocalDateTime.now().minusHours(1))  // 1시간 전
                .build();
    }

}
