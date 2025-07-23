package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.BackEntity;
import com.tapioca.BE.adapter.out.jpaRepository.ServerJpaRepository;
import com.tapioca.BE.domain.port.out.repository.server.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ServerRepositoryImpl implements ServerRepository {

    private final ServerJpaRepository serverJpaRepository;

    @Override
    public void save(BackEntity backEntity) {}
}
