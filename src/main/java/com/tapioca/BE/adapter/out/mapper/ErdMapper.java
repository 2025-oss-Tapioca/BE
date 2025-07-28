package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.AttributeEntity;
import com.tapioca.BE.adapter.out.entity.DiagramEntity;
import com.tapioca.BE.adapter.out.entity.ErdEntity;
import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.application.dto.response.erd.ErdResponseDto;
import com.tapioca.BE.domain.model.Attribute;
import com.tapioca.BE.domain.model.Diagram;
import com.tapioca.BE.domain.model.Erd;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ErdMapper {
    public Erd toDomain(ErdEntity erdEntity) {
        List<Diagram> diagrams = erdEntity.getDiagrams().stream()
                .map(this::toDomain)
                .toList();

        Erd erd = new Erd(erdEntity.getId(),
                erdEntity.getTeamEntity().getId(),
                erdEntity.getName());

        for (Diagram diagram : diagrams) {
            erd.addDiagram(diagram);
        }
        return erd;
    }

    private Diagram toDomain(DiagramEntity diagramEntity) {
        List<Attribute> attributes = diagramEntity.getAttributes().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
        return Diagram.of(
                diagramEntity.getId(),
                diagramEntity.getName(),
                attributes
        );
    }

    private Attribute toDomain(AttributeEntity attributeEntity) {
        return new Attribute(
                attributeEntity.getId(),
                attributeEntity.getName(),
                attributeEntity.getAttributeType(),
                attributeEntity.getLength(),
                attributeEntity.isPrimaryKey(),
                attributeEntity.isForeignKey()
        );
    }

    public ErdEntity toEntity(Erd erd, TeamEntity teamEntity) {
        ErdEntity erdEntity = ErdEntity.builder()
                .id(erd.getId())
                .teamEntity(teamEntity)
                .name(erd.getName())
                .build();
        for (Diagram diagram : erd.getDiagrams()) {
            DiagramEntity diagramEntity = DiagramEntity.builder()
                    .id(diagram.getId())
                    .name(diagram.getName())
                    .build();
            erdEntity.addDiagram(diagramEntity);

            for (Attribute attribute : diagram.getAttributes()) {
                AttributeEntity attributeEntity = AttributeEntity.builder()
                        .id(attribute.getId())
                        .name(attribute.getName())
                        .attributeType(attribute.getAttributeType())
                        .length(attribute.getLength())
                        .isPrimaryKey(attribute.getIsPrimaryKey())
                        .isForeignKey(attribute.getIsForeignKey())
                        .build();
                diagramEntity.addAttribute(attributeEntity);
            }
        }
        return erdEntity;
    }

    public ErdResponseDto toResponse(Erd erd) {
        return ErdResponseDto.from(erd);
    }
}
