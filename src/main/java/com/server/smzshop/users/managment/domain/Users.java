package com.server.smzshop.users.managment.domain;

import com.server.smzshop.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "users")
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String userId;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private Users(String userId, String password, String name) {
        this.userId = userId;
        this.passwordHash = password;
        this.name = name;
    }

    public static Users createUser(String userId, String password, String name) {
        return new Users(userId, password, name);
    }

    public enum UserRole {
        USER, ADMIN
    }

    public enum AccountStatus {
        ACTIVE, INACTIVE
    }
}
