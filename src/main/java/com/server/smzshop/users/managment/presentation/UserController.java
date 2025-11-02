package com.server.smzshop.users.managment.presentation;

import com.server.smzshop.users.managment.application.UserCommand;
import com.server.smzshop.users.managment.application.UserInfo;
import com.server.smzshop.users.managment.application.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserRegisterRequest userReq){
        UserCommand.Register userCmd = UserCommand.Register.from(userReq);
        UserInfo userInfo = userService.signup(userCmd);
        return ResponseEntity.ok(UserResponse.from(userInfo));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String username){
        UserInfo userInfo = userService.getUser(username);
        return ResponseEntity.ok(UserResponse.from(userInfo));
    }
}
