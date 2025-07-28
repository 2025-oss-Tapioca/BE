package com.tapioca.BE.application.service.erd;

import com.tapioca.BE.adapter.out.entity.AttributeLinkEntity;
import com.tapioca.BE.adapter.out.entity.ErdEntity;
import com.tapioca.BE.adapter.out.entity.MemberEntity;
import com.tapioca.BE.adapter.out.jpaRepository.AttributeLinkJpaRepository;
import com.tapioca.BE.adapter.out.jpaRepository.ErdJpaRepository;
import com.tapioca.BE.adapter.out.jpaRepository.MemberJpaRepository;
import com.tapioca.BE.application.dto.request.erd.UpdateErdRequestDto;
import com.tapioca.BE.application.dto.response.erd.ErdResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.port.in.usecase.erd.ErdUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ErdService implements ErdUseCase {
    private final MemberJpaRepository memberJpaRepository;
    private final ErdJpaRepository erdJpaRepository;
    private final AttributeLinkJpaRepository attributeLinkJpaRepository;

    @Override
    public ErdResponseDto getErd(UUID userId) {
        MemberEntity memberEntity = memberJpaRepository.findByUserEntity_Id(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
        UUID teamId = memberEntity.getTeamEntity().getId();

        ErdEntity erdEntity = erdJpaRepository.findByTeamEntity_Id(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ERD));

        List<AttributeLinkEntity> attributeLinks = attributeLinkJpaRepository.findByErdId(erdEntity.getId());

        return ErdResponseDto.of(erdEntity, attributeLinks);
    }

    @Override
    public ErdResponseDto updateErd(UUID userId, UpdateErdRequestDto updateErdRequestDto) {
        return null;
    }
}
