package com.server.smzshop.users.managment.presentation;

import com.server.smzshop.users.managment.application.UserInfo;
import com.server.smzshop.users.managment.domain.Users;

import java.time.LocalDateTime;

public record UserResponse (
        Long id,
        String userId,
        String name,
        Users.UserRole userRole,
        Users.AccountStatus accountStatus,
        LocalDateTime createdAt
){
   public static UserResponse from(UserInfo info) {
       return new UserResponse(
               info.id(),
               info.userId(),
               info.name(),
               info.userRole(),
               info.accountStatus(),
               info.createdAt()
       );
   }
}
