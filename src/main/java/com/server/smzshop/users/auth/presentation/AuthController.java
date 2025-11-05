package com.server.smzshop.users.auth.presentation;

import com.server.smzshop.common.exception.BusinessException;
import com.server.smzshop.common.exception.ExceptionEnum;
import com.server.smzshop.users.auth.application.AuthService;
import com.server.smzshop.users.auth.dto.AuthTokenDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginReq, HttpServletResponse response){
        AuthTokenDto authToken= authService.login(loginReq);

        setRefreshTokenCookie(response, authToken.getRefreshToken()); //Refresh Token 을 Http-only 쿠키로 설정

        return  ResponseEntity.ok(LoginResponse.from(authToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request , HttpServletResponse response){
        //쿠키에서 refresh token 추출
        String refreshToken = extractRefreshTokenFromCookie(request);
        AuthTokenDto newTokens = authService.refreshToken(refreshToken);

        //새로운 Refresh Token 쿠키로 설정
        setRefreshTokenCookie(response, newTokens.getRefreshToken());

        return ResponseEntity.ok(LoginResponse.from(newTokens));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response){
        //1. 쿠키에서 refresh token 추출
        String refreshToken = extractRefreshTokenFromCookie(request);

        //2. DB에서 refresh token 삭제
        authService.logout(refreshToken);

        //3. 쿠키삭제
        clearRefreshTokenCookie(response);
        return ResponseEntity.ok().build();
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setMaxAge(0); //즉시 만료
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);

        cookie.setHttpOnly(true); //javascript 접근 차단
        cookie.setSecure(true); //https에서만 전송
        cookie.setPath("/");
        //todo  프로퍼티즈에서 가져오기
        cookie.setMaxAge(7*24*60*60); //7일
        response.addCookie(cookie);
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if(request.getCookies() != null){
            for (Cookie cookie : request.getCookies()){
                if("refreshToken".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        throw new BusinessException(ExceptionEnum.REFRESH_TOKEN_NOT_FOUND);
    }


}
