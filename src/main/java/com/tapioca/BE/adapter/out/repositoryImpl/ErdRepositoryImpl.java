package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.ErdEntity;
import com.tapioca.BE.adapter.out.jpaRepository.ErdJpaRepository;
import com.tapioca.BE.domain.port.out.repository.erd.ErdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ErdRepositoryImpl implements ErdRepository {
    private final ErdJpaRepository erdJpaRepository;

    @Override
    public ErdEntity save(ErdEntity erd) {
        return erdJpaRepository.save(erd);
    }
}
