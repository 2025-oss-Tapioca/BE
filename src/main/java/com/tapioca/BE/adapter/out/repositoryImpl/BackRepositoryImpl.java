package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.jpaRepository.BackJpaRepository;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BackRepositoryImpl implements BackRepository {

    private final BackJpaRepository backJpaRepository;

    @Override
    public BackEntity findByTeamEntity_Id(UUID teamId) { return backJpaRepository.findByTeamEntity_Id(teamId); }

    @Override
    public BackEntity findByTeamCode(String teamCode){ return backJpaRepository.findByTeamEntity_code(teamCode); }
    
    @Override
    public BackEntity save(BackEntity backEntity) { return backJpaRepository.save(backEntity); }

}
