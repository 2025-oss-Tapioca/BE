package com.tapioca.BE.application.service.db;

import com.tapioca.BE.adapter.out.entity.project.DbEntity;
import com.tapioca.BE.adapter.out.mapper.DbMapper;
import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.project.DB;
import com.tapioca.BE.domain.port.in.usecase.db.DbDeleteUseCase;
import com.tapioca.BE.domain.port.out.repository.db.DbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DbDeleteService implements DbDeleteUseCase {

    private final DbMapper dbMapper;
    private final DbRepository dbRepository;

    @Override
    public void delete(ReadServerRequestDto readServerRequestDto) {

        DB db = dbMapper.toDomain(readServerRequestDto);

        DbEntity targetEntity = dbRepository.findByTeamCode(db.getTeamCode())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DB));

        // soft delete
        targetEntity.delete();
    }
}
