package com.server.smzshop.common.security;

import com.server.smzshop.users.auth.dto.AuthTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final Long accessTokenExpireTime;
    private final Long refreshTokenExpireTime;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKeyString,
                            @Value("${jwt.access-token-expire-time}")Long accessTokenExpireTime,
                            @Value("${jwt.refresh-token-expire-time}") Long refreshTokenExpireTime) {
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
        if(secretKeyString.getBytes().length<64){
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }else {
            this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
        }

        log.info("JWT Access Token Expire Time : {} sec",accessTokenExpireTime);
        log.info("JWT Refresh Token Expire Time : {} sec",refreshTokenExpireTime);
    }


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
                .signWith(secretKey,SignatureAlgorithm.HS512) // 서명
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
                .signWith(secretKey,SignatureAlgorithm.HS512)
                .compact();
    }

    //토큰에서 사용자 ID추출
    public String getUserIdFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    //토큰 유효성 검증
    public boolean validate(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
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

}
