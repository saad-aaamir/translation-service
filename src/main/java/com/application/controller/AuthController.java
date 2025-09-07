package com.application.controller;

import com.application.dto.response.ApiResponse;
import com.application.service.AuthServiceImpl;
import com.application.common.request.LoginRequestDto;
import com.application.common.request.SignupRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication APIs", description = "APIs for user and brand authentication, login, and registration.")
public class AuthController {
    private final AuthServiceImpl authServiceImpl;

    @PostMapping(path = "/user/signup")
    @Operation(summary = "User Signup", description = "Register as a user.")
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(ApiResponse.success("User signed up successfully", authServiceImpl.signup(signupRequestDto)));
    }


    @PostMapping(path = "/login")
    @Operation(summary = "User Authentication", description = "Authenticate and log in a user")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(ApiResponse.success("User logged in successfully", authServiceImpl.login(loginRequestDto)));
    }
}
