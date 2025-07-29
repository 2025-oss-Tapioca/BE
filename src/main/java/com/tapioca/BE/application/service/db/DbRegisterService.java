package com.tapioca.BE.application.service.db;

import com.tapioca.BE.adapter.out.entity.UserEntity;
import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import com.tapioca.BE.domain.port.in.usecase.db.DbRegisterUseCase;
import com.tapioca.BE.domain.port.out.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DbRegisterService implements DbRegisterUseCase {

    private final UserRepository userRepository;

    @Override
    public void register(UUID userId, RegisterRequestDto dbRequestDto) {


        // 2. 해당 유저가 속한 팀 정보 찾기

        // 3. 찾은 정보를 바탕으로 team Entity 생성

        // 4. 도메인으로 바꾸기

        // 5. 엔티티로 변환해서 DB에 저장
    }
}
