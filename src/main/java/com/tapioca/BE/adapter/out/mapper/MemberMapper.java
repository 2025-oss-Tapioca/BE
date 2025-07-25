package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.MemberEntity;
import com.tapioca.BE.domain.model.Member;

public class MemberMapper {
    public static Member toDomain(MemberEntity entity) {
        return new Member(
                entity.getId(),
                entity.getUserEntity().getId(),
                entity.getTeamEntity().getId(),
                entity.getMemberRole()
        );
    }
}
