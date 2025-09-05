package com.application.appuser;

import com.application.common.enums.RoleType;
import com.application.common.exceptions.ApplicationError;
import com.application.common.exceptions.ApplicationException;
import com.application.common.request.LoginRequestDto;
import com.application.common.request.SignupRequestDto;
import com.application.common.response.AuthenticationResponse;
import com.application.common.response.Response;
import com.application.common.response.ResponseCode;
import com.application.config.jwt.JwtService;
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
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MessageSource messageSource;

    public Response signup(SignupRequestDto signupRequestDto, Locale locale) {

        return appUserRepository.findByUsername(signupRequestDto.getUsername())
                .map(baseUser -> buildResponse(USER_ALREADY_EXISTS, locale))
                .orElseGet(() -> {
                    String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

                    AppUser appUser = AppUser.builder()
                            .username(signupRequestDto.getUsername())
                            .password(encodedPassword)
                            .role(RoleType.USER.name())
                            .active(true)
                            .build();
                    appUserRepository.save(appUser);
                    return buildResponse(SIGNUP_SUCCESS, locale);
                });
    }

    public AuthenticationResponse login(LoginRequestDto registerRequest, Locale locale) {

        Optional<AppUser> user = appUserRepository.findByUsername(registerRequest.getUsername());
        if (user.isPresent()) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            registerRequest.getUsername(),
                            registerRequest.getPassword()
                    ));
            return AuthenticationResponse.builder()
                    .accessToken(jwtService.generateAccessToken(user.get()))
                    .build();
        } else {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }
    }


    Response buildResponse(ResponseCode code, Locale locale) {
        return Response.builder()
                .code(Integer.valueOf(code.getCode()))
                .message(messageSource.getMessage(String.valueOf(Integer.valueOf(code.getCode())), null, locale))
                .build();
    }

    public <T> Response buildResponseWithData(T data, ResponseCode code, Locale locale) {
        return Response.builder()
                .data(data)
                .code(Integer.valueOf(code.getCode()))
                .message(messageSource.getMessage(String.valueOf(Integer.valueOf(code.getCode())), null, locale))
                .build();
    }
}