package com.tapioca.BE.application.service.erd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tapioca.BE.adapter.out.entity.erd.AttributeEntity;
import com.tapioca.BE.adapter.out.entity.erd.AttributeLinkEntity;
import com.tapioca.BE.adapter.out.entity.erd.DiagramEntity;
import com.tapioca.BE.adapter.out.entity.erd.ErdEntity;
import com.tapioca.BE.adapter.out.entity.user.MemberEntity;
import com.tapioca.BE.adapter.out.jpaRepository.*;
import com.tapioca.BE.application.dto.request.erd.UpdateErdRequestDto;
import com.tapioca.BE.application.dto.response.erd.ErdResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.enumType.AttributeType;
import com.tapioca.BE.domain.model.enumType.LinkType;
import com.tapioca.BE.domain.port.in.usecase.erd.ErdUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ErdService implements ErdUseCase {
    private final MemberJpaRepository memberJpaRepository;
    private final ErdJpaRepository erdJpaRepository;
    private final ObjectMapper objectMapper;

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

        ErdEntity erd = erdJpaRepository
                .findWithAllByTeamEntity_Id(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ERD));

        erd.getDiagrams().clear();
        erd.getAttributeLinks().clear();

        Map<String, AttributeEntity> attrByClientId = new HashMap<>();

        for (UpdateErdRequestDto.DiagramRequestDto diagDto : request.diagrams()) {
            DiagramEntity newDiag = DiagramEntity.builder()
                    .name(diagDto.diagramName())
                    .posX(diagDto.diagramPosX())
                    .posY(diagDto.diagramPosY())
                    .build();

            for (UpdateErdRequestDto.AttributeRequestDto attrDto : diagDto.attributes()) {
                AttributeEntity newAttr = AttributeEntity.builder()
                        .name(attrDto.attributeName())
                        .attributeType(AttributeType.valueOf(attrDto.attributeType()))
                        .length(attrDto.varcharLength())
                        .isPrimaryKey(attrDto.primaryKey())
                        .isForeignKey(attrDto.foreignKey())
                        .build();

                newDiag.addAttribute(newAttr);

                attrByClientId.put(attrDto.attributeId(), newAttr);
            }
            erd.addDiagram(newDiag);
        }

        for (UpdateErdRequestDto.AttributeLinkRequestDto linkDto : request.attributeLinks()) {
            AttributeEntity fromAttr = attrByClientId.get(linkDto.fromClientId());
            AttributeEntity toAttr = attrByClientId.get(linkDto.toClientId());

            if (fromAttr == null || toAttr == null) {
                throw new CustomException(ErrorCode.INVALID_REQUEST_BODY);
            }

            AttributeLinkEntity newLink = AttributeLinkEntity.builder()
                    .fromAttribute(fromAttr)
                    .toAttribute(toAttr)
                    .linkType(LinkType.valueOf(linkDto.linkType()))
                    .build();

            erd.addAttributeLink(newLink);
        }

        erdJpaRepository.save(erd);

        return ErdResponseDto.from(erd);
    }
}
