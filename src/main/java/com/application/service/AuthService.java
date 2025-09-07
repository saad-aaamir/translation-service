package com.application.service;

import com.application.common.request.LoginRequestDto;
import com.application.common.request.SignupRequestDto;
import com.application.common.response.AuthenticationResponse;

public interface AuthService {

    String signup(SignupRequestDto signupRequestDto);

    AuthenticationResponse login(LoginRequestDto loginRequestDto);
}
