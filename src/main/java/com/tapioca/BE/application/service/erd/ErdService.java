package com.tapioca.BE.application.service.erd;

import com.tapioca.BE.adapter.out.entity.*;
import com.tapioca.BE.adapter.out.jpaRepository.*;
import com.tapioca.BE.application.dto.request.erd.UpdateErdRequestDto;
import com.tapioca.BE.application.dto.response.erd.ErdResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.type.AttributeType;
import com.tapioca.BE.domain.model.type.LinkType;
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

    @Override
    public ErdResponseDto getErd(UUID userId) {
        MemberEntity member = memberJpaRepository.findByUserEntity_Id(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
        UUID teamId =member.getTeamEntity().getId();

        ErdEntity erd = erdJpaRepository.findWithAllByTeamEntity_Id(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ERD));

        return ErdResponseDto.from(erd);
    }

    @Override
    @Transactional
    public ErdResponseDto updateErd(UUID userId, UpdateErdRequestDto request) {
        UUID teamId = memberJpaRepository.findByUserEntity_Id(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER))
                .getTeamEntity().getId();

        ErdEntity erd = erdJpaRepository
                .findWithAllByTeamEntity_Id(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ERD));

        erd.setName(request.name());

        Map<UUID, UpdateErdRequestDto.DiagramRequestDto> dtoByDiagramId = request.diagrams().stream()
                .filter(d -> d.diagramId() != null)
                .collect(Collectors.toMap(UpdateErdRequestDto.DiagramRequestDto::diagramId,
                        Function.identity()));

        erd.getDiagrams().removeIf(diag ->
                diag.getId() != null && !dtoByDiagramId.containsKey(diag.getId())
        );

        for (DiagramEntity diagEntity : erd.getDiagrams()) {
            UpdateErdRequestDto.DiagramRequestDto dto = dtoByDiagramId.get(diagEntity.getId());

            diagEntity.setName(dto.diagramName());

            Map<UUID, UpdateErdRequestDto.AttributeRequestDto> attrDtoById = dto.attributes().stream()
                    .filter(a -> a.attributeId() != null)
                    .collect(Collectors.toMap(a -> a.attributeId(), Function.identity()));

            diagEntity.getAttributes().removeIf(attr ->
                    attr.getId() != null && !attrDtoById.containsKey(attr.getId())
            );

            for (AttributeEntity attrEntity : diagEntity.getAttributes()) {
                UpdateErdRequestDto.AttributeRequestDto adto = attrDtoById.get(attrEntity.getId());
                attrEntity.setName(adto.attributeName());
                attrEntity.setAttributeType(AttributeType.valueOf(adto.attributeType()));
                attrEntity.setLength(adto.varcharLength());
                attrEntity.setPrimaryKey(adto.primaryKey());
                attrEntity.setForeignKey(adto.foreignKey());
            }

            for (UpdateErdRequestDto.AttributeRequestDto adto : dto.attributes()) {
                if (adto.attributeId() == null) {
                    AttributeEntity newAttr = AttributeEntity.builder()
                            .name(adto.attributeName())
                            .attributeType(AttributeType.valueOf(adto.attributeType()))
                            .length(adto.varcharLength())
                            .isPrimaryKey(adto.primaryKey())
                            .isForeignKey(adto.foreignKey())
                            .build();
                    newAttr.setDiagram(diagEntity);
                    diagEntity.getAttributes().add(newAttr);
                }
            }
        }

        for (UpdateErdRequestDto.DiagramRequestDto dto : request.diagrams()) {
            if (dto.diagramId() == null) {
                DiagramEntity newDiag = DiagramEntity.builder()
                        .name(dto.diagramName())
                        .erdEntity(erd)
                        .build();

                for (UpdateErdRequestDto.AttributeRequestDto adto : dto.attributes()) {
                    AttributeEntity newAttr = AttributeEntity.builder()
                            .name(adto.attributeName())
                            .attributeType(AttributeType.valueOf(adto.attributeType()))
                            .length(adto.varcharLength())
                            .isPrimaryKey(adto.primaryKey())
                            .isForeignKey(adto.foreignKey())
                            .build();
                    newAttr.setDiagram(newDiag);
                    newDiag.getAttributes().add(newAttr);
                }

                erd.getDiagrams().add(newDiag);
            }
        }

        Map<UUID, UpdateErdRequestDto.AttributeLinkRequestDto> linkDtoById = request.attributeLinks().stream()
                .filter(l -> l.attributeLinkId() != null)
                .collect(Collectors.toMap(
                        UpdateErdRequestDto.AttributeLinkRequestDto::attributeLinkId,
                        Function.identity()
                ));

        erd.getAttributeLinks().removeIf(link ->
                link.getId() != null && !linkDtoById.containsKey(link.getId())
        );

        for (AttributeLinkEntity linkEntity : erd.getAttributeLinks()) {
            UpdateErdRequestDto.AttributeLinkRequestDto dto = linkDtoById.get(linkEntity.getId());
            linkEntity.setLinkType(LinkType.valueOf(dto.linkType()));
        }

        Map<String, AttributeEntity> attrByClientId = new HashMap<>();
        for (UpdateErdRequestDto.DiagramRequestDto dto : request.diagrams()) {
            DiagramEntity parent = erd.getDiagrams().stream()
                    .filter(d -> Objects.equals(d.getId(), dto.diagramId())
                            || (dto.diagramId() == null && d.getName().equals(dto.diagramName())))
                    .findFirst()
                    .orElseThrow();

            for (UpdateErdRequestDto.AttributeRequestDto adto : dto.attributes()) {
                AttributeEntity attr = parent.getAttributes().stream()
                        .filter(a -> Objects.equals(a.getId(), adto.attributeId())
                                || (adto.attributeId() == null && a.getName().equals(adto.attributeName())))
                        .findFirst()
                        .orElseThrow();
                attrByClientId.put(adto.clientId(), attr);
            }
        }

        for (UpdateErdRequestDto.AttributeLinkRequestDto dto : request.attributeLinks()) {
            if (dto.attributeLinkId() == null) {
                AttributeLinkEntity newLink = AttributeLinkEntity.builder()
                        .fromAttribute(attrByClientId.get(dto.fromClientId()))
                        .toAttribute(attrByClientId.get(dto.toClientId()))
                        .linkType(LinkType.valueOf(dto.linkType()))
                        .erdEntity(erd)
                        .build();
                erd.getAttributeLinks().add(newLink);
            }
        }

        erdJpaRepository.save(erd);
        return ErdResponseDto.from(erd);
    }
}
