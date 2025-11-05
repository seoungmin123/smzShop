package com.server.smzshop.common.security;

import com.server.smzshop.users.auth.dto.AuthTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-expire-time}")
    private Long accessTokenExpireTime;

    @Value("${jwt.refresh-token-expire-time}")
    private Long refreshTokenExpireTime;

    //Token Dto 변환
    public AuthTokenDto getAuthToken(String userId){
        String accessToken = generateAccessToken(userId);
        String refreshToken = generateRefreshToken(userId);

        return AuthTokenDto.of(accessToken,refreshToken,accessTokenExpireTime);
    }

    //Access Token 생성
    public String generateAccessToken(String userId){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessTokenExpireTime);

        return Jwts.builder()
                .setIssuer("smzshop")
                .setSubject(userId) //토큰 주인
                .setIssuedAt(now) //토큰 발급시간
                .setExpiration(expireDate) //토큰 만료시간
                .signWith(generateKey(),SignatureAlgorithm.HS512) // 서명
                .compact();
    }

    //Refresh Token 생성
    public String generateRefreshToken(String userId){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTokenExpireTime);

        return Jwts.builder()
                .setIssuer("smzshop")
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .claim("type","refresh")
                .signWith(generateKey(),SignatureAlgorithm.HS512)
                .compact();
    }

    //토큰에서 사용자 ID추출
    public String getUserIdFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    //토큰 유효성 검증
    public boolean validate(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .requireIssuer("smzshop")
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }

    }

    public LocalDateTime getRefreshTokenExpireTime(){
        return LocalDateTime.now().plusSeconds(refreshTokenExpireTime);
    }

    //키 생성
    private SecretKey generateKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

}
