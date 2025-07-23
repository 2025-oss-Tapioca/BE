package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.UserEntity;
import com.tapioca.BE.application.dto.request.user.LoginRequestDto;
import com.tapioca.BE.application.dto.request.user.SignUpRequestDto;
import com.tapioca.BE.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {
    public User toDomain(UUID id, SignUpRequestDto signUpRequestDto) {
        return new User(
                id,
                signUpRequestDto.email(),
                signUpRequestDto.name(),
                signUpRequestDto.userId(),
                signUpRequestDto.password()
        );
    }

    public User toDomain(UUID id, LoginRequestDto userRequestDto){
        return new User(
                id,
                userRequestDto.name(),
                userRequestDto.userId(),
                userRequestDto.password()
        );
    }

    public UserEntity toEntity(User user){
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userId(user.getUserId())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }

}
