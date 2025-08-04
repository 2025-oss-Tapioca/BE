package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.FrontEntity;
import com.tapioca.BE.adapter.out.jpaRepository.FrontJpaRepository;
import com.tapioca.BE.domain.port.out.repository.front.FrontRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FrontRepositoryImpl implements FrontRepository {

    private final FrontJpaRepository frontJpaRepository;

    @Override
    public FrontEntity save(FrontEntity frontEntity) { return frontJpaRepository.save(frontEntity); }
}
