package com.server.smzshop.common.security;

import com.server.smzshop.users.auth.dto.AuthTokenDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret-key=testSecretKeyForJunitTestThatShouldBeLongEnoughForHS512Algorithm",
        "jwt.access-token-expire-time=3600000",
        "jwt.refresh-token-expire-time=604800000"
})
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String testUserId;

    @BeforeEach
    void setUp() {
        testUserId = "testUser123";
    }

    @Test
    void Access토큰_생성_성공_테스트() {
        // when
        String accessToken = jwtTokenProvider.generateAccessToken(testUserId);

        // then
        Assertions.assertThat(accessToken).isNotNull();
        Assertions.assertThat(accessToken).isNotEmpty();
        Assertions.assertThat(accessToken.split("\\.")).hasSize(3); // JWT는 3부분으로 구성
    }

    @Test
    void Refresh토큰_생성_성공_테스트() {
        // when
        String refreshToken = jwtTokenProvider.generateRefreshToken(testUserId);

        // then
        Assertions.assertThat(refreshToken).isNotNull();
        Assertions.assertThat(refreshToken).isNotEmpty();
        Assertions.assertThat(refreshToken.split("\\.")).hasSize(3);
    }

    @Test
    void AuthToken_생성_성공_테스트() {
        // when
        AuthTokenDto authToken = jwtTokenProvider.getAuthToken(testUserId);

        // then
        Assertions.assertThat(authToken).isNotNull();
        Assertions.assertThat(authToken.getAccessToken()).isNotNull();
        Assertions.assertThat(authToken.getRefreshToken()).isNotNull();
        Assertions.assertThat(authToken.getAccessTokenExpiresIn()).isEqualTo(3600000L);
    }
}