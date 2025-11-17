package com.server.smzshop.fixtures;

import com.server.smzshop.users.managment.domain.Users;

public class UsersTestFixture {

    public static Users createTestUsers() {
        return Users.createUser("testUser", "hashedPassword", "테스트사용자");
    }

    public static Users createTestUser(String userId) {
        return Users.createUser(userId, "hashedPassword", "테스트사용자");
    }

    public static Users createTestUser(String userId, String passwordHash) {
        return Users.createUser(userId, passwordHash, "테스트사용자");
    }

    public static Users createTestUser(String userId, String passwordHash, String name) {
        return Users.createUser(userId, passwordHash, name);
    }

}
