package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.BackEntity;
import com.tapioca.BE.adapter.out.jpaRepository.BackJpaRepository;
import com.tapioca.BE.domain.port.out.repository.back.BackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BackRepositoryImpl implements BackRepository {

    private final BackJpaRepository jpaRepository;

    @Override
    public void save(BackEntity backEntity) {}
}
