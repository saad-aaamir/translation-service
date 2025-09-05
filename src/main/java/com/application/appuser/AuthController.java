package com.application.appuser;

import com.application.common.request.LoginRequestDto;
import com.application.common.request.SignupRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final AuthService authService;

    @PostMapping(path = "/user/signup")
    @Operation(summary = "User Signup", description = "Register as a user.")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto signupRequestDto,
                                    @RequestHeader(required = false) Locale locale) {
        return new ResponseEntity<>(authService.signup(signupRequestDto, locale), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    @Operation(summary = "User Authentication", description = "Authenticate and log in a user")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, @RequestHeader(required = false) Locale locale) {
        return new ResponseEntity<>(authService.login(loginRequestDto, locale), HttpStatus.OK);
    }
}
