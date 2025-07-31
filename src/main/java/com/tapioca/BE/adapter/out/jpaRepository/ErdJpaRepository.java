package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.ErdEntity;
import com.tapioca.BE.application.dto.response.erd.ErdResponseDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ErdJpaRepository extends JpaRepository<ErdEntity, UUID> {
    @EntityGraph(attributePaths = {
            "diagrams",
            "diagrams.attributes",
            "attributeLinks",
            "attributeLinks.fromAttribute",
            "attributeLinks.toAttribute"
    })
    Optional<ErdEntity> findWithAllByTeamEntity_Id(UUID teamId);
}
