package com.server.smzshop.users.auth.presentation;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @PostMapping("/register-test")
    public String registerTest(@RequestBody Map<String, String> data) {
        return "Received: " + data.toString();
    }
}