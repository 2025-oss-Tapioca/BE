package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.jpaRepository.BackJpaRepository;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import groovy.cli.OptionField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BackRepositoryImpl implements BackRepository {

    private final BackJpaRepository backJpaRepository;

    @Override
    public BackEntity findByTeamEntity_Id(UUID teamId) { return backJpaRepository.findByTeamEntity_IdAndDeletedAtIsNull(teamId); }

    @Override
    public Optional<BackEntity> findByTeamCode(String teamCode){ return backJpaRepository.findByTeamEntity_CodeAndDeletedAtIsNull(teamCode); }
    
    @Override
    public BackEntity save(BackEntity backEntity) { return backJpaRepository.save(backEntity); }

    @Override
    public boolean existsByTeamCode(String teamCode) { return backJpaRepository.existsByTeamEntity_CodeAndDeletedAtIsNull(teamCode); }

    @Override
    public void delete(BackEntity backEntity) { backJpaRepository.delete(backEntity); }

    @Override
    public boolean isSoftDeleted(String teamCode) { return backJpaRepository.existsByTeamEntity_CodeAndDeletedAtIsNotNull(teamCode); }

    @Override
    public Optional<BackEntity> findByTeamEntity_CodeAndDeletedAtIsNotNull(String teamCode) { return backJpaRepository.findByTeamEntity_CodeAndDeletedAtIsNotNull(teamCode); }

}
