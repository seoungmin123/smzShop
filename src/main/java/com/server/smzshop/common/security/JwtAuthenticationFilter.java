package com.server.smzshop.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthenticationFilter doFilterInternal");
        log.info("request uri : {}", request.getRequestURI());
        log.info("request method : {}", request.getMethod());

        try {
            //1. header에서 토큰 추출
            String token = resolveToken(request);

            //2. 토큰 검증 및 인증정보 저장
            if(token != null && jwtTokenProvider.validate(token)){
                //토큰에서 사용자 ID추출
                String userId = jwtTokenProvider.getUserIdFromToken(token);

                //Authentication 객체 생성
                Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

                //SecurityContext 에 저장
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Successfully Authenticated user : {}", userId);
            }else {
                log.error("Invalid or missing JWT Token");
            }
        }catch (Exception e){
            log.error("JWT authentication failed : {}", e.getMessage());
        }

        //3. 다음 필터로 이동
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7); //Bearer 제거
        }
        return null;
    }
}
