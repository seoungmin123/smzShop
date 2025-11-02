package com.server.smzshop.users.managment.domain;

public interface UserRepository {
    Users save(Users user);
    Users findByUserId(String userId);
}
