package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.MemberEntity;
import com.tapioca.BE.application.dto.response.team.TeamResponseDto;
import com.tapioca.BE.domain.model.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public static Member toDomain(MemberEntity entity) {
        return new Member(
                entity.getId(),
                entity.getUserEntity().getId(),
                entity.getTeamEntity().getId(),
                entity.getMemberRole()
        );
    }

    public static TeamResponseDto.MemberDto toDto(MemberEntity member) {
        return new TeamResponseDto.MemberDto(member.getUserEntity().getName(), member.getMemberRole());
    }
}
