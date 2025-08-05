package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.application.dto.request.gpt.UserInputRequestDto;
import com.tapioca.BE.domain.model.UserInput;
import org.springframework.stereotype.Component;

@Component
public class UserInputMapper {
    public UserInput toDomain(UserInputRequestDto userInputRequestDto){
        return new UserInput(userInputRequestDto.userRequest());
    }
}
