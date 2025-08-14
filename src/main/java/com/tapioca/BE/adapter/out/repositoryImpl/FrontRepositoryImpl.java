package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.project.FrontEntity;
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

    @Override
    public FrontEntity findByCode(String teamCode) { return frontJpaRepository.findByTeamEntity_code(teamCode); }

    @Override
    public boolean existsByCode(String teamCode) { return frontJpaRepository.existsByTeamEntity_code(teamCode); }
}
