package com.tapioca.BE.application.service.back;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.mapper.BackEndMapper;
import com.tapioca.BE.application.dto.request.common.DeleteServerRequestDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.project.BackEnd;
import com.tapioca.BE.domain.port.in.usecase.back.BackDeleteUseCase;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BackDeleteService implements BackDeleteUseCase {

    private final BackRepository backRepository;
    private final BackEndMapper backEndMapper;

    @Override
    public void delete(DeleteServerRequestDto deleteServerRequestDto) {

        BackEnd backEnd = backEndMapper.toDomain(deleteServerRequestDto);

        BackEntity targetEntity = backRepository.findByTeamCode(backEnd.getTeamCode())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BACK));

        // soft delete
        targetEntity.delete();
    }
}
