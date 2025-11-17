package com.server.smzshop.infra.users;

import com.server.smzshop.users.managment.domain.UserRepository;
import com.server.smzshop.users.managment.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;


    @Override
    public Users save(Users user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<Users> findByUserId(String userId) {
        return userJpaRepository.findByUserId(userId);
    }
}
