package com.tapioca.BE.application.service.user;

import com.tapioca.BE.adapter.out.entity.UserEntity;
import com.tapioca.BE.adapter.out.mapper.UserMapper;
import com.tapioca.BE.application.dto.request.user.LoginRequestDto;
import com.tapioca.BE.application.dto.request.user.SignUpRequestDto;
import com.tapioca.BE.config.security.JwtTokenDto;
import com.tapioca.BE.config.security.JwtTokenProvider;
import com.tapioca.BE.domain.model.User;
import com.tapioca.BE.domain.port.in.usecase.user.UserLoginUseCase;
import com.tapioca.BE.domain.port.out.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserLoginService implements UserLoginUseCase {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public JwtTokenDto login(LoginRequestDto dto){
        UserEntity userEntity = userRepository.findByLoginId(dto.loginId());
        if (userEntity == null) {
            throw new UsernameNotFoundException("로그인 ID에 해당하는 사용자가 없습니다.");
        }

        User user =  userMapper.toDomain(userEntity.getId(),dto);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLoginId(),user.getPassword())
        );

        return jwtTokenProvider.generateToken(authentication);
    }
}
