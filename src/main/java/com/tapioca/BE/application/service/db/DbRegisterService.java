package com.tapioca.BE.application.service.db;

import com.tapioca.BE.adapter.out.entity.DbEntity;
import com.tapioca.BE.adapter.out.entity.MemberEntity;
import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.adapter.out.entity.UserEntity;
import com.tapioca.BE.adapter.out.mapper.DbMapper;
import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.db.RegisterResponseDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import com.tapioca.BE.domain.model.DB;
import com.tapioca.BE.domain.port.in.usecase.db.DbRegisterUseCase;
import com.tapioca.BE.domain.port.out.repository.db.DbRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import com.tapioca.BE.domain.port.out.repository.user.MemberRepository;
import com.tapioca.BE.domain.port.out.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DbRegisterService implements DbRegisterUseCase {

    private final DbRepository dbRepository;
    private final DbMapper dbMapper;
    private final TeamRepository teamRepository;

    @Override
    public RegisterResponseDto register(RegisterRequestDto dbRequestDto) {

        TeamEntity teamEntity = teamRepository.findByTeamId(dbRequestDto.teamId());

        // 4. 도메인으로 바꾸기
        DB db = dbMapper.toDomain(dbRequestDto);

        // 5. 엔티티로 변환해서 DB에 저장
        DbEntity dbEntity = dbMapper.toEntity(db, teamEntity);
        dbRepository.save(dbEntity);

        return new RegisterResponseDto(
                dbEntity.getTeamEntity().getId(),
                dbEntity.getAddress(),
                dbEntity.getUser(),
                dbEntity.getPassword(),
                dbEntity.getName(),
                dbEntity.getPort(),
                dbEntity.getRdsInstanceId(),
                dbEntity.getAwsRegion(),
                dbEntity.getAwsAccessKey(),
                dbEntity.getAwsSecretKey()
        );
    }
}
