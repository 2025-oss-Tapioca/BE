package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.user.LoginRequestDto;
import com.tapioca.BE.application.dto.request.user.SignUpRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.user.UserLoginUseCase;
import com.tapioca.BE.domain.port.in.usecase.user.UserSignUpUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserLoginUseCase userLoginUseCase;
    private final UserSignUpUseCase userSignUpUseCase;

    @PostMapping("/api/login")
    public CommonResponseDto<?> userLogin(
            @RequestBody LoginRequestDto loginRequestDto
    ){
        return CommonResponseDto.ok(userLoginUseCase.login(loginRequestDto));
    }

    @PostMapping("/api/signup")
    public CommonResponseDto<?> userSignUp(
            @RequestBody SignUpRequestDto signRequestDto
    ){
        userSignUpUseCase.signUp(signRequestDto);
        return CommonResponseDto.created("회원 가입 성공");
    }
}
