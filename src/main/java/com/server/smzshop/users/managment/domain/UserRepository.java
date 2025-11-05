package com.server.smzshop.users.managment.domain;

import java.util.Optional;

public interface UserRepository {
    Users save(Users user);
    Optional<Users> findByUserId(String userId);
}
