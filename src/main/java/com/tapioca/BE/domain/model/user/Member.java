package com.tapioca.BE.domain.model.user;

import com.tapioca.BE.domain.model.enumType.MemberRole;

import java.util.UUID;

public class Member {
    private final UUID id;
    private final UUID userId;
    private final UUID teamId;
    private final MemberRole memberRole;

    public Member(
            UUID id, UUID userId,
            UUID teamId, MemberRole memberRole
    ){
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
        this.memberRole = memberRole;
    }

    public UUID getTeamId() {
        return teamId;
    }
}
