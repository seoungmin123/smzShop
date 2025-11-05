package com.server.smzshop.infra.users;

import com.server.smzshop.users.managment.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<Users, Long> {

    //회원가입
    Users save(Users user);

    //회원조회
    Optional<Users> findByUserId(String userId);
}
