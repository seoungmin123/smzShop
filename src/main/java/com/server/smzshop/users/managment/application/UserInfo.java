package com.server.smzshop.users.managment.application;

import com.server.smzshop.users.managment.domain.Users;

import java.time.LocalDateTime;

public record UserInfo (
        Long id,
        String username,
        String name,
        Users.UserRole userRole,
        Users.AccountStatus accountStatus,
        LocalDateTime createdAt
)
{
    public static UserInfo from(Users users) {
        return new UserInfo(
                users.getId(),
                users.getUsername(),
                users.getName(),
                users.getUserRole(),
                users.getAccountStatus(),
                users.getCreatedAt()
        );
    }
}
