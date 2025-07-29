package com.tapioca.BE.application.service.erd;

import com.tapioca.BE.adapter.out.entity.*;
import com.tapioca.BE.adapter.out.jpaRepository.*;
import com.tapioca.BE.application.dto.request.erd.UpdateAttributeLinkRequestDto;
import com.tapioca.BE.application.dto.request.erd.UpdateAttributeRequestDto;
import com.tapioca.BE.application.dto.request.erd.UpdateDiagramRequestDto;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ErdService implements ErdUseCase {
    private final MemberJpaRepository memberJpaRepository;
    private final ErdJpaRepository erdJpaRepository;
    private final AttributeJpaRepository attributeJpaRepository;
    private final AttributeLinkJpaRepository attributeLinkJpaRepository;
    private final DiagramJpaRepository diagramJpaRepository;

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
        System.out.println(updateErdRequestDto);
        MemberEntity member = memberJpaRepository.findByUserEntity_Id(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        UUID teamId = member.getTeamEntity().getId();

        ErdEntity erd = erdJpaRepository.findByTeamEntity_Id(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ERD));

        erd.getDiagrams().clear();
        diagramJpaRepository.deleteByErdId(erd.getId());

        for (UpdateDiagramRequestDto diagrams : updateErdRequestDto.getDiagrams()) {
            DiagramEntity diagramEntity = DiagramEntity.builder()
                    .name(diagrams.getName())
                    .build();
            for (UpdateAttributeRequestDto attributes : diagrams.getAttributes()) {
                AttributeEntity attributeEntity = AttributeEntity.builder()
                        .name(attributes.getName())
                        .attributeType(AttributeType.valueOf(attributes.getAttributeType()))
                        .length(attributes.getLength())
                        .isPrimaryKey(attributes.isPK())
                        .isForeignKey(attributes.isFK())
                        .build();
                diagramEntity.addAttribute(attributeEntity);
            }
            erd.addDiagram(diagramEntity);
        }
        erdJpaRepository.save(erd);

        List<AttributeEntity> allAttrs = attributeJpaRepository.findByErdId(erd.getId());
        Map<String, AttributeEntity> attrMap = allAttrs.stream()
                .collect(Collectors.toMap(
                        a -> a.getDiagram().getName() + "." + a.getName(),
                        Function.identity()
                ));

        attributeLinkJpaRepository.deleteByErdId(erd.getId());

        for (UpdateAttributeLinkRequestDto attributeLink : updateErdRequestDto.getAttributeLinks()) {
            String fromKey = attributeLink.fromDiagram() + "." + attributeLink.fromAttribute();
            String toKey   = attributeLink.toDiagram()   + "." + attributeLink.toAttribute();

            AttributeEntity from = attrMap.get(fromKey);
            AttributeEntity to   = attrMap.get(toKey);

            if (from == null || to == null) {
                throw new CustomException(ErrorCode.NOT_FOUND_ATTRIBUTE);
            }

            AttributeLinkEntity link = AttributeLinkEntity.builder()
                    .linkType(LinkType.valueOf(attributeLink.getLinkType()))
                    .build();
            link.setFromAttribute(from);
            link.setToAttribute(to);
            attributeLinkJpaRepository.save(link);
        }

        List<AttributeLinkEntity> attributeLinks = attributeLinkJpaRepository.findByErdId(erd.getId());
        return ErdResponseDto.of(erd, attributeLinks);
    }
}
