package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.user.UserEntity;
import com.tapioca.BE.application.dto.request.user.LoginRequestDto;
import com.tapioca.BE.application.dto.request.user.SignUpRequestDto;
import com.tapioca.BE.domain.model.user.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {
    public User toDomain(UUID id, SignUpRequestDto signUpRequestDto) {
        return new User(
                id,
                signUpRequestDto.email(),
                signUpRequestDto.name(),
                signUpRequestDto.loginId(),
                signUpRequestDto.password()
        );
    }

    public User toDomain(UUID id, LoginRequestDto loginRequestDto){
        return new User(
                id,
                loginRequestDto.loginId(),
                loginRequestDto.password()
        );
    }

    public UserEntity toEntity(User user){
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .loginId(user.getLoginId())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }

}