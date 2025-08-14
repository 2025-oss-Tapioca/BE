package com.tapioca.BE.application.service.db;

import com.tapioca.BE.adapter.out.entity.project.DbEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.adapter.out.mapper.DbMapper;
import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.db.RegisterResponseDto;
import com.tapioca.BE.domain.model.project.DB;
import com.tapioca.BE.domain.port.in.usecase.db.DbUpdateUseCase;
import com.tapioca.BE.domain.port.out.repository.db.DbRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DbUpdateService implements DbUpdateUseCase {

    private final DbRepository dbRepository;
    private final DbMapper dbMapper;
    private final TeamRepository teamRepository;

    @Override
    public RegisterResponseDto update(RegisterRequestDto registerRequestDto) {

        // 수정한 내용
        DB updated = dbMapper.toDomain(registerRequestDto);

        TeamEntity teamEntity = teamRepository.findByTeamCode(updated.getTeamCode());

        // 수정 대상
        DbEntity existingEntity = dbRepository.findByTeamCode(teamEntity.getCode());

        DbEntity savedEntity = dbMapper.toEntity(updated, existingEntity, teamEntity);
        dbRepository.save(savedEntity);

        return new RegisterResponseDto(
                savedEntity.getTeamEntity().getCode(),
                savedEntity.getAddress(),
                savedEntity.getUser(),
                savedEntity.getPassword(),
                savedEntity.getName(),
                savedEntity.getPort(),
                savedEntity.getRdsInstanceId(),
                savedEntity.getAwsRegion(),
                savedEntity.getRoleArn()
        );
    }
}
