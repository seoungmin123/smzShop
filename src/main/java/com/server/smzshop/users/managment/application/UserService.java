package com.server.smzshop.users.managment.application;

import com.server.smzshop.common.exception.BusinessException;
import com.server.smzshop.common.exception.ExceptionEnum;
import com.server.smzshop.users.managment.domain.UserRepository;
import com.server.smzshop.users.managment.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfo signup(UserCommand.Register userReq) {
        // 1) 중복 검사, 정책 검사
        if (existsByUsername(userReq.username())) {
            throw new BusinessException(ExceptionEnum.USER_DUPLICATE, userReq.username());
        }

        // 2) 비밀번호 암호화: passwordEncoder.encode(cmd.rawPassword())
        String hashPwd = passwordEncoder.encode(userReq.password());

        // 3) Users.create(cmd.username(), hash, cmd.name()) -> save
        Users user = Users.createUser(userReq.username(), hashPwd, userReq.name());

        // 4) 저장 결과를 UserInfo/UserResponse로 변환해서 리턴
        Users saved = userRepository.save(user);

       return UserInfo.from(saved);
    }

    public UserInfo getUser(String username) {
        if (!existsByUsername(username)) {
            throw new BusinessException(ExceptionEnum.USER_NOT_FOUND, username);
        }
        
        Users user = userRepository.findByUsername(username);
        return UserInfo.from(user);
    }

    private boolean existsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }
}
