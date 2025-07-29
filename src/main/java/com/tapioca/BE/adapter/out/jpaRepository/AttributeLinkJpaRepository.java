package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.AttributeLinkEntity;
import com.tapioca.BE.domain.model.AttributeLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface AttributeLinkJpaRepository extends JpaRepository<AttributeLinkEntity, UUID> {
    @Query("""
        select l 
        from AttributeLinkEntity l
        where l.fromAttribute.diagram.erd.id = :erdId
           or l.toAttribute.diagram.erd.id   = :erdId
    """)
    List<AttributeLinkEntity> findByErdId(@Param("erdId") UUID erdId);

    @Modifying
    @Transactional
    @Query("""
        delete
        from AttributeLinkEntity l
        where l.fromAttribute.diagram.erd.id = :erdId
           or l.toAttribute.diagram.erd.id   = :erdId
    """)
    void deleteByErdId(@Param("erdId") UUID erdId);
}
