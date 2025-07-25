package com.tapioca.BE.domain.port.out.repository.user;

import com.tapioca.BE.adapter.out.entity.MemberEntity;

import java.util.UUID;

public interface MemberRepository {
    MemberEntity findByUserId(UUID userId);
}
