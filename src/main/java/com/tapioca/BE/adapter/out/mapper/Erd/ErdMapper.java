package com.tapioca.BE.adapter.out.mapper.Erd;

import com.tapioca.BE.adapter.out.entity.ErdEntity;
import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.domain.model.Erd;
import org.springframework.stereotype.Component;

@Component
public class ErdMapper {
    public static Erd toDomain(ErdEntity erdEntity) {
        return new Erd(
                erdEntity.getId(),
                erdEntity.getTeamEntity().getId(),
                erdEntity.getName()
        );
    }

    public static ErdEntity toEntity(Erd erd, TeamEntity teamEntity) {
        ErdEntity erdEntity = ErdEntity.builder()
                .id(erd.getId())
                .name(erd.getName())
                .build();

        erdEntity.setTeamEntity(teamEntity);

        return erdEntity;
    }
}
