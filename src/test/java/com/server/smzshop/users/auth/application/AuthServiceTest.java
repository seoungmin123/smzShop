package com.server.smzshop.users.auth.application;

import com.server.smzshop.common.exception.BusinessException;
import com.server.smzshop.common.exception.ExceptionEnum;
import com.server.smzshop.users.auth.domain.RefreshToken;
import com.server.smzshop.users.auth.domain.RefreshTokenRepository;
import com.server.smzshop.users.auth.dto.AuthTokenDto;
import com.server.smzshop.users.auth.presentation.LoginRequest;
import com.server.smzshop.users.managment.domain.UserRepository;
import com.server.smzshop.users.managment.domain.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static com.server.smzshop.fixtures.AuthTestFixture.createExpiredRefreshToken;
import static com.server.smzshop.fixtures.AuthTestFixture.createLoginRequest;
import static com.server.smzshop.fixtures.UsersTestFixture.createTestUser;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    AuthService authService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    // 기본 테스트들
    @Test
    void 로그인_성공_테스트(){
        //given
        String userId = "testUser";
        String pwd = "password123";
        String hashedPassword = passwordEncoder.encode(pwd);

        Users user = createTestUser(userId, hashedPassword);
        userRepository.save(user);
        LoginRequest loginRequest = createLoginRequest(userId, pwd);

        //when
        AuthTokenDto login = authService.login(loginRequest);

        //then
        Assertions.assertThat(login.getAccessToken()).isNotNull();
        Assertions.assertThat(login.getRefreshToken()).isNotNull();
        Assertions.assertThat(user.getUserId()).isEqualTo(userId);
    }

    @Test
    void 잘못된_비밀번호_예외_테스트(){
        //given
        String userId = "testUser";
        String pwd = "password123";
        String wrongPwd = "wrongPassword";
        String hashedPassword = passwordEncoder.encode(pwd);

        Users user = createTestUser(userId, hashedPassword);
        userRepository.save(user);
        LoginRequest loginRequest = createLoginRequest(userId, wrongPwd);

        //when & then
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(
                BusinessException.class,
                () -> authService.login(loginRequest));

        Assertions.assertThat(exception.getError()).isEqualTo(ExceptionEnum.INVALID_PASSWORD);
    }

    @Test
    void 존재하지않는_사용자_예외_테스트(){
        //given
        String nonExistentUserId = "nonExistentUser";
        String pwd = "password123";
        LoginRequest loginRequest = createLoginRequest(nonExistentUserId, pwd);

        //when
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(
                BusinessException.class,
                () -> authService.login(loginRequest)
        );

        //then
        Assertions.assertThat(exception.getError()).isEqualTo(ExceptionEnum.USER_NOT_FOUND);
    }

    @Test
    void 리프레시토큰_갱신_성공_테스트(){
        //given
        String userId = "refreshTestUser";
        String pwd = "password123";
        String hashedPassword = passwordEncoder.encode(pwd);

        Users user = createTestUser(userId, hashedPassword);
        userRepository.save(user);
        LoginRequest loginRequest = createLoginRequest(userId, pwd);
        AuthTokenDto initialTokens = authService.login(loginRequest);

        //when
        AuthTokenDto refreshedTokens = authService.refreshToken(initialTokens.getRefreshToken());

        //then
        Assertions.assertThat(refreshedTokens.getAccessToken()).isNotNull();
        Assertions.assertThat(refreshedTokens.getRefreshToken()).isNotNull();
        Assertions.assertThat(refreshedTokens.getAccessTokenExpiresIn()).isEqualTo(initialTokens.getAccessTokenExpiresIn());
        Assertions.assertThat(refreshedTokens.getRefreshToken()).isNotNull(); //TODO refreshedToken 발급 방식 변경
    }

    @Test
    void 유효하지않은_리프레시토큰_예외_테스트(){
        //given
        String expiredRefreshToken = "invalidRefreshToken";

        //when
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(
                BusinessException.class,
                () -> authService.refreshToken(expiredRefreshToken)
        );

        //then
        Assertions.assertThat(exception.getError()).isEqualTo(ExceptionEnum.INVALID_TOKEN);
    }

    @Test
    void 만료된_리프레시토큰_예외_테스트(){
        //given
        String invalidToken = "invalid.or.expired.token";

        //when
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(
                BusinessException.class,
                () -> authService.refreshToken(invalidToken)
        );

        //then
        Assertions.assertThat(exception.getError())
                .isIn(ExceptionEnum.TOKEN_EXPIRED, ExceptionEnum.INVALID_TOKEN);
    }

    @Test
    void 만료된_리프레시토큰_예외_테스트2(){
        //given
        String userId = "expiredTestUser";
        String pwd = "password123";
        String hashedPassword = passwordEncoder.encode(pwd);
        Users user = createTestUser(userId, hashedPassword);
        userRepository.save(user);
        LoginRequest loginRequest = createLoginRequest(userId, pwd);
        AuthTokenDto initialTokens = authService.login(loginRequest);

        //기존토큰 삭제
        refreshTokenRepository.deleteByToken(initialTokens.getRefreshToken());

        //실제 만료된 RefreshToken 생성하고 DB에 저장
        RefreshToken expiredRefreshToken = createExpiredRefreshToken(
                                        initialTokens.getRefreshToken(),
                                        user.getUserId());

        refreshTokenRepository.save(expiredRefreshToken);

        //when
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(
                BusinessException.class,
                () -> authService.refreshToken(expiredRefreshToken.getToken())
        );

        //then
        Assertions.assertThat(exception.getError()).isEqualTo(ExceptionEnum.TOKEN_EXPIRED);
    }

    @Test
    void 로그아웃_성공_테스트(){
        //given
        String userId = "logoutTestUser";
        String pwd = "password123";
        String hashedPassword = passwordEncoder.encode(pwd);
        Users user = createTestUser(userId, hashedPassword);
        userRepository.save(user);
        LoginRequest loginRequest = createLoginRequest(userId, pwd);
        AuthTokenDto initialTokens = authService.login(loginRequest);

        //when
        Assertions.assertThat(refreshTokenRepository.findByToken(initialTokens.getRefreshToken())).isPresent();
        authService.logout(initialTokens.getRefreshToken());

        //then
        Assertions.assertThat(refreshTokenRepository.findByToken(initialTokens.getRefreshToken())).isEmpty();

    }

}