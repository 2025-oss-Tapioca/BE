package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.MemberEntity;
import com.tapioca.BE.adapter.out.jpaRepository.MemberJpaRepository;
import com.tapioca.BE.domain.port.out.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public MemberEntity findByUserId(UUID userId) {
        return memberJpaRepository.findByUserEntityId(userId);
    }
}
