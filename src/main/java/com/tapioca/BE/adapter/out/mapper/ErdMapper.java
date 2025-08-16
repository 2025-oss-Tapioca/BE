package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.erd.AttributeEntity;
import com.tapioca.BE.adapter.out.entity.erd.AttributeLinkEntity;
import com.tapioca.BE.adapter.out.entity.erd.DiagramEntity;
import com.tapioca.BE.adapter.out.entity.erd.ErdEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.application.dto.request.erd.UpdateErdRequestDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.enumType.AttributeType;
import com.tapioca.BE.domain.model.enumType.LinkType;
import com.tapioca.BE.domain.model.erd.Attribute;
import com.tapioca.BE.domain.model.erd.AttributeLink;
import com.tapioca.BE.domain.model.erd.Diagram;
import com.tapioca.BE.domain.model.erd.Erd;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ErdMapper {
    public static Erd toDomain(UpdateErdRequestDto dto) {
        if (dto == null) return new Erd(List.of(), List.of(), List.of());

        List<Diagram> diagrams = Optional.ofNullable(dto.diagrams()).orElseGet(List::of)
                .stream()
                .map(d -> new Diagram(
                        d.diagramId(),
                        null,
                        d.diagramName(),
                        d.diagramPosX(),
                        d.diagramPosY()
                ))
                .toList();

        List<Attribute> attributes = new ArrayList<>();
        Map<String, String> attrIdIndex = new HashMap<>();

        for (UpdateErdRequestDto.DiagramRequestDto d
                : Optional.ofNullable(dto.diagrams()).orElseGet(List::of)) {
            String diagramId = d.diagramId();
            for (UpdateErdRequestDto.AttributeRequestDto a
                    : Optional.ofNullable(d.attributes()).orElseGet(List::of)) {

                AttributeType type = parseAttributeType(a.attributeType());
                Attribute domainAttr = new Attribute(
                        a.attributeId(),
                        diagramId,
                        a.attributeName(),
                        type,
                        a.varcharLength(),
                        a.primaryKey(),
                        a.foreignKey()
                );
                attributes.add(domainAttr);
                if (a.attributeId() != null && !a.attributeId().isBlank()) {
                    attrIdIndex.put(a.attributeId(), a.attributeId());
                }
            }
        }

        List<AttributeLink> links = Optional.ofNullable(dto.attributeLinks()).orElseGet(List::of)
                .stream()
                .map(l -> new AttributeLink(
                        null,
                        l.fromClientId(),
                        l.toClientId(),
                        parseLinkType(l.linkType()),
                        l.sourceCard(),
                        l.targetCard(),
                        l.identifying()

                ))
                .toList();

        return new Erd(diagrams, attributes, links);
    }

    public static ErdEntity toEntity(Erd erd, TeamEntity teamEntity) {
        ErdEntity erdEntity = ErdEntity.builder()
                .teamEntity(teamEntity)
                .build();

        Map<String, DiagramEntity> diagramByStringId = new HashMap<>();
        for (Diagram d : erd.getDiagrams()) {
            DiagramEntity de = DiagramEntity.builder()
                    .id(safeUuid(d.getId()))
                    .name(d.getName())
                    .posX(d.getPosX())
                    .posY(d.getPosY())
                    .erdEntity(erdEntity)
                    .build();
            erdEntity.addDiagram(de);
            if (d.getId() != null) {
                diagramByStringId.put(d.getId(), de);
            }
        }

        Map<String, AttributeEntity> attrByStringId = new HashMap<>();
        for (Attribute a : erd.getAttributes()) {
            DiagramEntity parent = (a.getDiagramId() == null) ? null : diagramByStringId.get(a.getDiagramId());
            if (parent == null) {
                throw new CustomException(ErrorCode.INVALID_REQUEST_BODY);
            }

            AttributeEntity ae = AttributeEntity.builder()
                    .id(safeUuid(a.getId()))
                    .diagram(parent)
                    .name(a.getName())
                    .attributeType(a.getAttributeType())
                    .length(a.getLength())
                    .isPrimaryKey(a.isPrimaryKey())
                    .isForeignKey(a.isForeignKey())
                    .build();

            parent.addAttribute(ae);
            if (a.getId() != null && !a.getId().isBlank()) {
                attrByStringId.put(a.getId(), ae);
            }
        }

        for (AttributeLink l : erd.getAttributeLinks()) {
            AttributeEntity from = (l.getFromAttributeId() == null) ? null : attrByStringId.get(l.getFromAttributeId());
            AttributeEntity to   = (l.getToAttributeId() == null) ? null : attrByStringId.get(l.getToAttributeId());

            if (from == null || to == null) {
                throw new CustomException(
                        ErrorCode.INVALID_REQUEST_BODY);
            }

            AttributeLinkEntity le = AttributeLinkEntity.builder()
                    .id(safeUuid(l.getId()))
                    .fromAttribute(from)
                    .toAttribute(to)
                    .linkType(l.getLinkType())
                    .sourceCard(l.getSourceCard())
                    .targetCard(l.getTargetCard())
                    .identifying(l.isIdentifying())
                    .erdEntity(erdEntity)
                    .build();

            erdEntity.addAttributeLink(le);
        }

        return erdEntity;
    }

    private static UUID safeUuid(String raw) {
        if (raw == null || raw.isBlank()) return null;
        try { return UUID.fromString(raw.trim()); }
        catch (IllegalArgumentException e) { return null; }
    }

    private static AttributeType parseAttributeType(String raw) {
        if (raw == null || raw.isBlank()) return null;
        try { return AttributeType.valueOf(raw.trim().toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException e) { return null; }
    }

    private static LinkType parseLinkType(String raw) {
        if (raw == null || raw.isBlank()) return null;
        try { return LinkType.valueOf(raw.trim().toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException e) { return null; }
    }
}
