package com.tapioca.BE.application.service.team;

import com.tapioca.BE.adapter.out.entity.erd.ErdEntity;
import com.tapioca.BE.adapter.out.entity.user.MemberEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.adapter.out.entity.user.UserEntity;
import com.tapioca.BE.adapter.out.jpaRepository.MemberJpaRepository;
import com.tapioca.BE.adapter.out.jpaRepository.TeamJpaRepository;
import com.tapioca.BE.adapter.out.jpaRepository.UserJpaRepository;
import com.tapioca.BE.adapter.out.mapper.MemberMapper;
import com.tapioca.BE.adapter.out.mapper.TeamMapper;
import com.tapioca.BE.application.dto.request.team.CreateTeamRequestDto;
import com.tapioca.BE.application.dto.response.team.TeamsDto;
import com.tapioca.BE.application.dto.response.team.TeamResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.user.Member;
import com.tapioca.BE.domain.model.enumType.MemberRole;
import com.tapioca.BE.domain.model.user.Team;
import com.tapioca.BE.domain.port.in.usecase.team.TeamUseCase;
import com.tapioca.BE.domain.port.out.repository.erd.ErdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService implements TeamUseCase {
    private final TeamJpaRepository teamJpaRepository;
    private final ErdRepository erdRepository;
    private final UserJpaRepository userJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    @Override
    public List<TeamsDto> getTeam(UUID userId) {
        List<MemberEntity> memberList = memberJpaRepository.findAllByUserEntity_Id(userId);

        List<TeamsDto> teams = memberList.stream()
                .map(member -> {
                    TeamEntity team = member.getTeamEntity();
                    return new TeamsDto(
                            team.getName(),
                            team.getCode()
                    );
                })
                .collect(Collectors.toList());

        return teams;
    }

    @Override
    public TeamResponseDto getTeamInfo(UUID userId, String teamCode) {
        MemberEntity memberEntity = memberJpaRepository.findByUserEntity_IdAndTeamEntity_Code(userId, teamCode).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        if (memberEntity == null) {
            return new TeamResponseDto(null, null, null, List.of());
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

        return new TeamResponseDto(teamEntity.getName(), teamEntity.getDescription(), teamEntity.getCode(), memberDtoList);
    }

    @Override
    @Transactional
    public TeamResponseDto createTeam(UUID userId, CreateTeamRequestDto createTeamRequestDto){
        UserEntity user = userJpaRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        String teamCode = UUID.randomUUID().toString().substring(0, 8);

        TeamEntity teamEntity = TeamEntity.builder()
                .name(createTeamRequestDto.teamName())
                .description(createTeamRequestDto.teamDescription())
                .code(UUID.randomUUID().toString().substring(0, 8))
                .build();
        TeamEntity savedTeam = teamJpaRepository.save(teamEntity);

        ErdEntity erdEntity = ErdEntity.builder()
                .name(createTeamRequestDto.teamName() + "'s ERD")
                .build();
        erdEntity.setTeamEntity(savedTeam);
        erdRepository.save(erdEntity);

        MemberEntity member = new MemberEntity(null, userJpaRepository.getReferenceById(userId),
                savedTeam, MemberRole.ADMIN);
        memberJpaRepository.save(member);

        List<TeamResponseDto.MemberDto> memberList = memberJpaRepository
                .findAllByTeamEntity_Id(savedTeam.getId())
                .stream()
                .map(MemberMapper::toDto)
                .toList();

        return new TeamResponseDto(
                savedTeam.getName(),
                savedTeam.getDescription(),
                savedTeam.getCode(),
                memberList
        );
    }

    @Override
    @Transactional
    public TeamResponseDto updateTeam(UUID userId,  String teamCode, CreateTeamRequestDto createTeamRequestDto) {
        Team team = TeamMapper.toDomain(createTeamRequestDto);
        TeamEntity teamEntity = teamJpaRepository.findByCode(teamCode).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));
        memberJpaRepository.findByUserEntity_IdAndTeamEntity_Code(userId, teamCode).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        teamEntity.setName(team.getName());
        teamEntity.setDescription(team.getDescription());

        TeamEntity savedTeam = teamJpaRepository.save(teamEntity);
        List<TeamResponseDto.MemberDto> memberList = memberJpaRepository
                .findAllByTeamEntity_Id(savedTeam.getId())
                .stream()
                .map(MemberMapper::toDto)
                .toList();

        return new TeamResponseDto(
                savedTeam.getName(),
                savedTeam.getDescription(),
                savedTeam.getCode(),
                memberList
        );
    }

    @Override
    public TeamResponseDto joinTeam(UUID userId, String teamCode) {
        TeamEntity teamEntity = teamJpaRepository.findByCode(teamCode)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        if (teamEntity == null) return null;

        UserEntity user = userJpaRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));

        MemberEntity memberEntity = new MemberEntity(
                null,
                user,
                teamEntity,
                MemberRole.USER
        );
        memberJpaRepository.save(memberEntity);

        List<MemberEntity> memberEntities = memberJpaRepository.findAllByTeamEntity_Id(teamEntity.getId());

        List<TeamResponseDto.MemberDto> memberDtos = memberEntities.stream()
                .map(MemberMapper::toDto)
                .toList();

        return new TeamResponseDto(teamEntity.getName(), teamEntity.getDescription(), teamEntity.getCode(), memberDtos);
    }

    @Override
    public void leaveTeam(UUID userId,  String teamCode) {
        MemberEntity member = memberJpaRepository.findByUserEntity_IdAndTeamEntity_Code(userId, teamCode)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        memberJpaRepository.delete(member);
    }
}
