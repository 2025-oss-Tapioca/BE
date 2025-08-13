package com.tapioca.BE.application.service.back;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.mapper.BackEndMapper;
import com.tapioca.BE.application.dto.request.back.DeleteRequestDto;
import com.tapioca.BE.application.dto.response.back.DeleteResponseDto;
import com.tapioca.BE.domain.model.project.BackEnd;
import com.tapioca.BE.domain.port.in.usecase.back.BackDeleteUseCase;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
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
    public DeleteResponseDto delete(DeleteRequestDto deleteRequestDto) {

        BackEnd backEnd = backEndMapper.toDomain(deleteRequestDto);

        BackEntity backEntity = backRepository.findByTeamCode(backEnd.getTeamCode());

    }
}
