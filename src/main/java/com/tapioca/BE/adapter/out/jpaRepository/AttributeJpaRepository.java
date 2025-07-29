package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AttributeJpaRepository extends JpaRepository<AttributeEntity, UUID> {
    @Query("""
        select a
        from AttributeEntity a
        where a.diagram.erd.id = :erdId
    """)
    List<AttributeEntity> findByErdId(@Param("erdId") UUID erdId);
}
