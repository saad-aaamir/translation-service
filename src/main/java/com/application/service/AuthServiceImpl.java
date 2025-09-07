package com.application.service;

import com.application.common.enums.RoleType;
import com.application.common.exceptions.ApplicationError;
import com.application.common.exceptions.ApplicationException;
import com.application.common.request.LoginRequestDto;
import com.application.common.request.SignupRequestDto;
import com.application.common.response.AuthenticationResponse;
import com.application.config.jwt.JwtService;
import com.application.entity.AppUser;
import com.application.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

import static com.application.common.response.ResponseCode.SIGNUP_SUCCESS;
import static com.application.common.response.ResponseCode.USER_ALREADY_EXISTS;

@Service
public class AuthServiceImpl {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public String signup(SignupRequestDto signupRequestDto) {
        return appUserRepository.findByUsername(signupRequestDto.getUsername())
                .map(baseUser -> "User already exists")
                .orElseGet(() -> {
                    String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

                    AppUser appUser = AppUser.builder()
                            .username(signupRequestDto.getUsername())
                            .password(encodedPassword)
                            .role(RoleType.USER.name())
                            .active(true)
                            .build();
                    appUserRepository.save(appUser);

                    return "Signup successful";
                });
    }

    public AuthenticationResponse login(LoginRequestDto loginRequest) {
        Optional<AppUser> user = appUserRepository.findByUsername(loginRequest.getUsername());
        if (user.isPresent()) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    ));

            return AuthenticationResponse.builder()
                    .accessToken(jwtService.generateAccessToken(user.get()))
                    .build();
        } else {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }
    }
}

