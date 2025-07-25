package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.ErdEntity;
import com.tapioca.BE.adapter.out.jpaRepository.ErdJpaRepository;
import com.tapioca.BE.adapter.out.mapper.ErdMapper;
import com.tapioca.BE.domain.model.Erd;
import com.tapioca.BE.domain.port.out.repository.erd.ErdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ErdRepositoryImpl implements ErdRepository {
    private final ErdJpaRepository erdJpaRepository;
    private final ErdMapper erdMapper;

    @Override
    public Optional<Erd> findByTeamId(UUID teamId) {
        return erdJpaRepository
                .findByTeamId(teamId)
                .map(erdMapper::toDomain);
    }

    @Override
    public Erd save(Erd erd) {
        ErdEntity entity = erdMapper.toEntity(erd);
        return erdMapper.toDomain(erdJpaRepository.save(entity));
    }
}
