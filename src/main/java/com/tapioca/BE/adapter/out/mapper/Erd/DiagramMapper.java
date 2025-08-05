package com.tapioca.BE.adapter.out.mapper.Erd;

import com.tapioca.BE.adapter.out.entity.erd.DiagramEntity;
import com.tapioca.BE.adapter.out.entity.erd.ErdEntity;
import com.tapioca.BE.domain.model.erd.Diagram;
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
