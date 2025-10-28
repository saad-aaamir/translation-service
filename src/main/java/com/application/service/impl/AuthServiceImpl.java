package com.application.service.impl;

import com.application.enums.RoleType;
import com.application.dto.request.LoginRequestDto;
import com.application.dto.request.SignupRequestDto;
import com.application.dto.response.AuthenticationResponse;
import com.application.config.jwt.JwtService;
import com.application.entity.AppUser;
import com.application.exception.BusinessLogicException;
import com.application.repository.AppUserRepository;
import com.application.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String signup(SignupRequestDto signupRequestDto) {
        if (appUserRepository.findByUsername(signupRequestDto.getUsername()).isPresent()) {
            throw new BusinessLogicException("User already exists with username: " + signupRequestDto.getUsername());
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        AppUser appUser = AppUser.builder()
                .username(signupRequestDto.getUsername())
                .password(encodedPassword)
                .role(RoleType.USER.name())
                .active(true)
                .build();

        appUserRepository.save(appUser);
        return "Signup successful!";
    }

    @Override
    public AuthenticationResponse login(LoginRequestDto loginRequest) {
        Optional<AppUser> user = appUserRepository.findByUsername(loginRequest.getUsername());
        if (user.isPresent()) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        ));
            } catch (BadCredentialsException e) {
                throw new BusinessLogicException(e.getMessage());
            }
            return AuthenticationResponse.builder()
                    .accessToken(jwtService.generateAccessToken(user.get()))
                    .build();
        } else {
            throw new BusinessLogicException("User not found!");
        }
    }
}

