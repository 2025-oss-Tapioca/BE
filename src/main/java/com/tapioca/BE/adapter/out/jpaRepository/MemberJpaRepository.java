package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, UUID> {
    Optional<MemberEntity> findByUserEntity_Id(UUID userId);

    @Query("""
    SELECT m FROM MemberEntity m
    JOIN FETCH m.userEntity
    WHERE m.teamEntity.id = :teamId
""")
    List<MemberEntity> findAllByTeamEntity_Id(@Param("teamId") UUID teamId);
}
