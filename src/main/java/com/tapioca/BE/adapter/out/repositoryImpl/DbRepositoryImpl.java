package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.project.DbEntity;
import com.tapioca.BE.adapter.out.jpaRepository.DbJpaRepository;
import com.tapioca.BE.domain.port.out.repository.db.DbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DbRepositoryImpl implements DbRepository {

    private final DbJpaRepository jpaRepository;

    @Override
    public DbEntity save(DbEntity dbEntity) { return jpaRepository.save(dbEntity); }

    @Override
    public Optional<DbEntity> findByTeamCode(String teamCode) { return jpaRepository.findByTeamEntity_CodeAndDeletedAtIsNull(teamCode); }

    @Override
    public boolean existsByTeamCode(String teamCode) { return jpaRepository.existsByTeamEntity_CodeAndDeletedAtIsNull(teamCode); }

}
