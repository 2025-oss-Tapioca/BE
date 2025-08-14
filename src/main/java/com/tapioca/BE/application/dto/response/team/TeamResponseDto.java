package com.tapioca.BE.application.dto.response.team;

import com.tapioca.BE.domain.model.enumType.MemberRole;

import java.util.List;

public record TeamResponseDto(
        String teamName,
        String teamDescription,
        String teamCode,
        List<MemberDto> member
) {
    public record MemberDto(
            String memberName,
            MemberRole memberRole
    ) {}
}
