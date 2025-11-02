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
        if (existsByUserId(userReq.userId())) {
            throw new BusinessException(ExceptionEnum.USER_DUPLICATE, userReq.userId());
        }

        // 2) 비밀번호 암호화: passwordEncoder.encode(cmd.rawPassword())
        String hashPwd = passwordEncoder.encode(userReq.password());

        // 3) Users 생성
        Users user = Users.createUser(userReq.userId(), hashPwd, userReq.name());

        // 4) 저장 결과를 UserInfo/UserResponse로 변환해서 리턴
        Users saved = userRepository.save(user);

       return UserInfo.from(saved);
    }

    public UserInfo getUser(String userId) {
        if (!existsByUserId(userId)) {
            throw new BusinessException(ExceptionEnum.USER_NOT_FOUND, userId);
        }
        
        Users user = userRepository.findByUserId(userId);
        return UserInfo.from(user);
    }

    private boolean existsByUserId(String userId) {
        return userRepository.findByUserId(userId) != null;
    }
}
