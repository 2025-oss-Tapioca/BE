package com.tapioca.BE.application.service.erd;

import com.tapioca.BE.adapter.out.entity.erd.AttributeLinkEntity;
import com.tapioca.BE.adapter.out.entity.erd.DiagramEntity;
import com.tapioca.BE.adapter.out.entity.erd.ErdEntity;
import com.tapioca.BE.adapter.out.entity.user.MemberEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.adapter.out.jpaRepository.*;
import com.tapioca.BE.adapter.out.mapper.ErdMapper;
import com.tapioca.BE.application.dto.request.erd.UpdateErdRequestDto;
import com.tapioca.BE.application.dto.response.erd.ErdResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.erd.Erd;
import com.tapioca.BE.domain.port.in.usecase.erd.ErdUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
public class ErdService implements ErdUseCase {
    private final MemberJpaRepository memberJpaRepository;
    private final ErdJpaRepository erdJpaRepository;

    @Override
    public ErdResponseDto getErd(UUID userId, String teamCode) {
        MemberEntity member = memberJpaRepository.findByUserEntity_IdAndTeamEntity_Code(userId, teamCode)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
        UUID teamId = member.getTeamEntity().getId();

        ErdEntity erd = erdJpaRepository.findWithAllByTeamEntity_Id(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ERD));

        return ErdResponseDto.from(erd);
    }

    @Override
    @Transactional
    public ErdResponseDto updateErd(UUID userId, String teamCode, UpdateErdRequestDto request) {
        UUID teamId = memberJpaRepository.findByUserEntity_IdAndTeamEntity_Code(userId, teamCode)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER))
                .getTeamEntity().getId();

        ErdEntity erdEntity = erdJpaRepository
                .findWithAllByTeamEntity_Id(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ERD));

        erdEntity.getDiagrams().clear();
        erdEntity.getAttributeLinks().clear();

        Erd erdDomain = ErdMapper.toDomain(request);

        TeamEntity teamEntity = erdEntity.getTeamEntity();
        ErdEntity updateErdEntity = ErdMapper.toEntity(erdDomain,teamEntity);

        for(DiagramEntity diagramEntity : updateErdEntity.getDiagrams()) {
            erdEntity.addDiagram(diagramEntity);
        }
        for(AttributeLinkEntity linkEntity : updateErdEntity.getAttributeLinks()) {
            erdEntity.addAttributeLink(linkEntity);
        }

        erdJpaRepository.save(erdEntity);

        return ErdResponseDto.from(erdEntity);
    }
}
