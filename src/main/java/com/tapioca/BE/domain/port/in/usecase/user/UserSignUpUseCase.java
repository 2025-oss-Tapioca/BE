package com.tapioca.BE.domain.port.in.usecase.user;

import com.tapioca.BE.application.dto.request.user.SignUpRequestDto;

public interface UserSignUpUseCase {
    public void signUp(SignUpRequestDto userRequestDto);
}
