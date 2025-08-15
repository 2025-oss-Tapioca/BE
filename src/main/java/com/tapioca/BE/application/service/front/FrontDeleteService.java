package com.tapioca.BE.application.service.front;

import com.tapioca.BE.adapter.out.entity.project.FrontEntity;
import com.tapioca.BE.adapter.out.mapper.FrontMapper;
import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.project.Front;
import com.tapioca.BE.domain.port.in.usecase.front.FrontDeleteUseCase;
import com.tapioca.BE.domain.port.out.repository.front.FrontRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FrontDeleteService implements FrontDeleteUseCase {

    private final FrontRepository frontRepository;
    private final FrontMapper frontMapper;

    @Override
    public void delete(ReadServerRequestDto readServerRequestDto) {

        Front front = frontMapper.toDomain(readServerRequestDto);

        FrontEntity targetEntity = frontRepository.findByCode(front.getTeamCode())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FRONT));

        // soft delete
        targetEntity.delete();
    }
}
