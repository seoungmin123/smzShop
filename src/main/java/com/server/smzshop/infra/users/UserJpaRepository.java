package com.server.smzshop.infra.users;

import com.server.smzshop.users.managment.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<Users, Long> {

    //회원조회
    Users findByUsername(String username);

    //회원가입
    Users save(Users user);

}
