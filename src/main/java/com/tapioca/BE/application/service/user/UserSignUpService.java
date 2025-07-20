package com.tapioca.BE.application.service.user;

import com.tapioca.BE.adapter.out.entity.UserEntity;
import com.tapioca.BE.adapter.out.mapper.UserMapper;
import com.tapioca.BE.application.dto.request.user.SignUpRequestDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.User;
import com.tapioca.BE.domain.port.in.usecase.user.UserSignUpUseCase;
import com.tapioca.BE.domain.port.out.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignUpService implements UserSignUpUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignUpRequestDto dto){
        if(userRepository.existsByUserId(dto.userId())){
            throw new CustomException(ErrorCode.DUPLICATION_LOGIN_ID);
        }
        User user =  userMapper.toDomain(null,dto);

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.changePassword(encodePassword);

        UserEntity mappingUserEntity = userMapper.toEntity(user);
        userRepository.save(mappingUserEntity);
    }
}
