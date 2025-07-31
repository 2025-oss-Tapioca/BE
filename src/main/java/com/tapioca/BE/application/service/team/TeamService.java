package com.tapioca.BE.application.service.team;

import com.tapioca.BE.adapter.out.entity.ErdEntity;
import com.tapioca.BE.adapter.out.entity.MemberEntity;
import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.adapter.out.entity.UserEntity;
import com.tapioca.BE.adapter.out.jpaRepository.MemberJpaRepository;
import com.tapioca.BE.adapter.out.jpaRepository.TeamJpaRepository;
import com.tapioca.BE.adapter.out.jpaRepository.UserJpaRepository;
import com.tapioca.BE.adapter.out.mapper.MemberMapper;
import com.tapioca.BE.application.dto.request.team.CreateTeamRequestDto;
import com.tapioca.BE.application.dto.response.team.TeamResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.Member;
import com.tapioca.BE.domain.model.type.MemberRole;
import com.tapioca.BE.domain.port.in.usecase.team.TeamUseCase;
import com.tapioca.BE.domain.port.out.repository.erd.ErdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService implements TeamUseCase {
    private final TeamJpaRepository teamJpaRepository;
    private final ErdRepository erdRepository;
    private final UserJpaRepository userJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final MemberMapper memberMapper;

    @Override
    public TeamResponseDto getTeamInfo(UUID userId) {
        MemberEntity memberEntity = memberJpaRepository.findByUserEntity_Id(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        if (memberEntity == null) {
            return new TeamResponseDto(null, null, List.of());
        }

        Member member = MemberMapper.toDomain(memberEntity);
        UUID teamId = member.getTeamId();

        TeamEntity teamEntity = teamJpaRepository.findById(teamId).orElse(null);

        List<MemberEntity> memberEntities = memberJpaRepository.findAllByTeamEntity_Id(teamId);

        List<TeamResponseDto.MemberDto> memberDtoList = memberEntities.stream().map(me -> {
            String name = me.getUserEntity().getName();
            MemberRole role = me.getMemberRole();
            return MemberMapper.toDto(me);
        }).toList();

        return new TeamResponseDto(teamEntity.getName(), teamEntity.getCode(), memberDtoList);
    }

    @Override
    @Transactional
    public TeamResponseDto createTeam(UUID userId, CreateTeamRequestDto createTeamRequestDto){
        UserEntity user = userJpaRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        String teamCode = UUID.randomUUID().toString().substring(0, 8);

        TeamEntity teamEntity = TeamEntity.builder()
                .name(createTeamRequestDto.teamName())
                .code(UUID.randomUUID().toString().substring(0, 8))
                .build();
        TeamEntity savedTeam = teamJpaRepository.save(teamEntity);

        ErdEntity erdEntity = ErdEntity.builder()
                .name(createTeamRequestDto.teamName() + "'s ERD")
                .build();
        erdEntity.setTeamEntity(savedTeam);
        erdRepository.save(erdEntity);

        MemberEntity member = new MemberEntity(null, userJpaRepository.getReferenceById(userId),
                savedTeam, MemberRole.NONE);
        memberJpaRepository.save(member);

        List<TeamResponseDto.MemberDto> memberList = memberJpaRepository
                .findAllByTeamEntity_Id(savedTeam.getId())
                .stream()
                .map(MemberMapper::toDto)
                .toList();

        return new TeamResponseDto(
                savedTeam.getName(),
                savedTeam.getCode(),
                memberList
        );
    }

    @Override
    public TeamResponseDto joinTeam(UUID userId, String teamCode) {
        TeamEntity teamEntity = teamJpaRepository.findByCode(teamCode)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        if (teamEntity == null) return null;

        if (memberJpaRepository.findByUserEntity_Id(userId).isPresent()) {
            throw new CustomException(ErrorCode.CONFLICT_TEAM_BUILDING);
        }

        UserEntity user = userJpaRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));

        MemberEntity memberEntity = new MemberEntity(
                null,
                user,
                teamEntity,
                MemberRole.NONE
        );
        memberJpaRepository.save(memberEntity);

        List<MemberEntity> memberEntities = memberJpaRepository.findAllByTeamEntity_Id(teamEntity.getId());

        List<TeamResponseDto.MemberDto> memberDtos = memberEntities.stream()
                .map(MemberMapper::toDto)
                .toList();

        return new TeamResponseDto(teamEntity.getName(), teamCode, memberDtos);
    }

    @Override
    public void leaveTeam(UUID userId) {
        MemberEntity member = memberJpaRepository.findByUserEntity_Id(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        memberJpaRepository.delete(member);
    }
}
