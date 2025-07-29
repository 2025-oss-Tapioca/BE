package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.DiagramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface DiagramJpaRepository extends JpaRepository<DiagramEntity, UUID> {
    @Modifying
    @Transactional
    @Query("DELETE FROM DiagramEntity d WHERE d.erd.id = :erdId")
    void deleteByErdId(@Param("erdId") UUID erdId);
}
