package com.server.smzshop.users.auth.application;

import com.server.smzshop.common.exception.BusinessException;
import com.server.smzshop.common.exception.ExceptionEnum;
import com.server.smzshop.common.security.JwtTokenProvider;
import com.server.smzshop.users.auth.domain.RefreshToken;
import com.server.smzshop.users.auth.domain.RefreshTokenRepository;
import com.server.smzshop.users.auth.dto.AuthTokenDto;
import com.server.smzshop.users.auth.presentation.LoginRequest;
import com.server.smzshop.users.managment.domain.UserRepository;
import com.server.smzshop.users.managment.domain.Users;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthTokenDto login(@Valid LoginRequest loginReq) {
        //1. 사용자 조회
        Users user = userRepository.findByUserId(loginReq.getUserId())
                .orElseThrow(() -> new BusinessException(ExceptionEnum.USER_NOT_FOUND, loginReq.getUserId()));

        //2. 비밀번호 검증
        if(!passwordEncoder.matches(loginReq.getPassword(), user.getPasswordHash())){
            throw new BusinessException(ExceptionEnum.INVALID_PASSWORD);
        }

        //3. 토큰생성 (access, refresh)
        AuthTokenDto authToken = jwtTokenProvider.getAuthToken(user.getUserId());

        //4. refresh token DB 저장
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(authToken.getRefreshToken())
                .userId(user.getUserId())
                .expiresAt(jwtTokenProvider.getRefreshTokenExpireTime())
                .build();

        // 기존 refresh token 삭제 후 새로 저장
        refreshTokenRepository.deleteByUserId(user.getUserId());
        refreshTokenRepository.save(refreshTokenEntity);

        return authToken;
    }

    public AuthTokenDto refreshToken (String refreshToken){
        // 1. refresh token 유효성 검증
        if (!jwtTokenProvider.validate(refreshToken)){
            throw new BusinessException(ExceptionEnum.INVALID_TOKEN);
        }

        // 2. DB에서 refresh token 조회
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BusinessException(ExceptionEnum.REFRESH_TOKEN_NOT_FOUND));

        // 3. 만료 확인
        if(storedToken.getExpiresAt().isBefore(LocalDateTime.now())){
            refreshTokenRepository.deleteByToken(refreshToken);
            throw new BusinessException(ExceptionEnum.TOKEN_EXPIRED);
        }

        // 4. 새로운 access token 생성
        return jwtTokenProvider.getAuthToken(storedToken.getUserId());
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
