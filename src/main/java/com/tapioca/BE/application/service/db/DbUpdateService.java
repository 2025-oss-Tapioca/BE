package com.tapioca.BE.application.service.db;

import com.tapioca.BE.adapter.out.entity.project.DbEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.adapter.out.mapper.DbMapper;
import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.db.RegisterResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
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

        // 수정 대상
        DbEntity existingEntity = dbRepository.findByTeamCode(updated.getTeamCode());

        if (existingEntity == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_DB);
        }

        TeamEntity teamEntity = teamRepository.findByTeamCode(updated.getTeamCode());

        DbEntity savedEntity = dbMapper.toEntity(updated, existingEntity, teamEntity);
        dbRepository.save(savedEntity);

        return new RegisterResponseDto(
                updated.getTeamCode(),
                updated.getDbAddress(),
                updated.getDbUser(),
                updated.getPassword(),
                updated.getDbName(),
                updated.getDbPort(),
                updated.getRdsInstanceId(),
                updated.getAwsRegion(),
                updated.getRoleArn()
        );
    }
}
