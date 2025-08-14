package com.tapioca.BE.application.service.db;

import com.tapioca.BE.adapter.out.entity.project.DbEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.adapter.out.mapper.DbMapper;
import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.db.RegisterResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.project.DB;
import com.tapioca.BE.domain.port.in.usecase.db.DbRegisterUseCase;
import com.tapioca.BE.domain.port.in.usecase.db.DbUpdateUseCase;
import com.tapioca.BE.domain.port.out.repository.db.DbRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DbRegisterService implements DbRegisterUseCase {

    private final DbRepository dbRepository;
    private final DbMapper dbMapper;
    private final TeamRepository teamRepository;
    private final DbUpdateUseCase dbUpdateUseCase;

    @Override
    public RegisterResponseDto register(RegisterRequestDto dbRequestDto) {

        // 도메인으로 바꾸기
        DB db = dbMapper.toDomain(dbRequestDto);

        // soft deleted 되지 않은 dbEntity 있으면 중복 처리
        if (dbRepository.existsByTeamCode(db.getTeamCode())) {
            throw new CustomException(ErrorCode.CONFLICT_REGISTERED_DB);
        }

        // soft deleted 된 dbEntity 있으면 복구
        if (dbRepository.isSoftDeleted(db.getTeamCode())) {
            DbEntity dbEntity =
                    dbRepository.findByTeamEntity_CodeAndDeletedAtIsNotNull(db.getTeamCode())
                            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DB));

            dbEntity.restore();
            return dbUpdateUseCase.update(dbRequestDto);
        }

        TeamEntity teamEntity = teamRepository.findByTeamCode(db.getTeamCode());

        // 엔티티로 변환해서 DB에 저장
        DbEntity dbEntity = dbMapper.toEntity(db, teamEntity);
        dbRepository.save(dbEntity);

        return new RegisterResponseDto(
                db.getTeamCode(),
                db.getDbAddress(),
                db.getDbUser(),
                db.getPassword(),
                db.getDbName(),
                db.getDbPort(),
                db.getRdsInstanceId(),
                db.getAwsRegion(),
                db.getRoleArn()
        );
    }
}
