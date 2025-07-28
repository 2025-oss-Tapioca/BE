package com.tapioca.BE.application.dto.response.team;

import com.tapioca.BE.domain.model.type.MemberRole;

import java.util.List;

public record TeamResponseDto(
        String teamName,
        String teamCode,
        List<MemberDto> member
) {
    public record MemberDto(
            String memberName,
            MemberRole memberRole
    ) {}
}
