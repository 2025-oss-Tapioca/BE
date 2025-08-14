package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.user.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, UUID> {
    Optional<MemberEntity> findByUserEntity_Id(UUID userId);
    Optional<MemberEntity> findByUserEntity_IdAndTeamEntity_Code(UUID userId, String teamCode);
    List<MemberEntity> findAllByTeamEntity_Id(UUID teamId);
    List<MemberEntity> findAllByUserEntity_Id(UUID userId);
    long countByTeamEntity_Id(UUID teamId);
}
