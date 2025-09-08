package com.application.service;

import com.application.dto.request.LoginRequestDto;
import com.application.dto.request.SignupRequestDto;
import com.application.dto.response.AuthenticationResponse;

public interface AuthService {

    String signup(SignupRequestDto signupRequestDto);

    AuthenticationResponse login(LoginRequestDto loginRequestDto);
}
