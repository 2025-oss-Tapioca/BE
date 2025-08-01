package com.tapioca.BE.adapter.out.mapper.Erd;

import com.tapioca.BE.adapter.out.entity.DiagramEntity;
import com.tapioca.BE.adapter.out.entity.ErdEntity;
import com.tapioca.BE.domain.model.Diagram;
import org.springframework.stereotype.Component;

@Component
public class DiagramMapper {
    public static Diagram toDomain(DiagramEntity diagramEntity) {
        return new Diagram(
                diagramEntity.getId(),
                diagramEntity.getErdEntity().getId(),
                diagramEntity.getName()
        );
    }

    public static DiagramEntity toEntity(Diagram diagram, ErdEntity erdEntity) {
        DiagramEntity diagramEntity = DiagramEntity.builder()
                .id(diagram.getId())
                .name(diagram.getName())
                .build();
        diagramEntity.setErdEntity(erdEntity);

        return diagramEntity;
    }
}
