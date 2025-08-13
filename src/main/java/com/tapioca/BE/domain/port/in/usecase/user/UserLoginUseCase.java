package com.tapioca.BE.domain.port.in.usecase.user;

import com.tapioca.BE.application.dto.request.user.LoginRequestDto;
import com.tapioca.BE.config.security.JwtTokenDto;

public interface UserLoginUseCase {
    public JwtTokenDto login(LoginRequestDto userRequestDto);
}
