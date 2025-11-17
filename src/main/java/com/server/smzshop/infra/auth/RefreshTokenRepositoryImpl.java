package com.server.smzshop.infra.auth;

import com.server.smzshop.users.auth.domain.RefreshToken;
import com.server.smzshop.users.auth.domain.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken){
        return refreshTokenJpaRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenJpaRepository.findByToken(token);
    }

    @Override
    public Optional<RefreshToken> findByUserId(String userId){
        return refreshTokenJpaRepository.findByUserId(userId);
    }

    @Override
    public void deleteByUserId(String userId){
        refreshTokenJpaRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteByToken(String token){
        refreshTokenJpaRepository.deleteByToken(token);
    }
}
