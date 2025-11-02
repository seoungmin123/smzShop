package com.server.smzshop.users.managment.application;

import com.server.smzshop.users.managment.presentation.UserRegisterRequest;

public class UserCommand{
    private UserCommand(){}

    public static record Register(
            String username,
            String password,
            String name
    ){
        public static Register from(UserRegisterRequest userReq){
            return new Register(userReq.username(), userReq.password(), userReq.name());
        }
    }
}