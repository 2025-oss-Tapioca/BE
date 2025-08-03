package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.adapter.out.jpaRepository.TeamJpaRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepository {
    private final TeamJpaRepository teamJpaRepository;

    @Override
    public TeamEntity save(TeamEntity team) {
        return teamJpaRepository.save(team);
    }

    @Override
    public void deleteById(UUID teamId) {
        teamJpaRepository.deleteById(teamId);
    }
}
