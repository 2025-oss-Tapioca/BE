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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService implements TeamUseCase {
    private final TeamJpaRepository teamRepository;
    private final UserJpaRepository userRepository;
    private final MemberJpaRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public TeamResponseDto getTeamInfo(UUID userId) {
        MemberEntity memberEntity = memberRepository.findByUserEntity_Id(userId).orElse(null);

        if (memberEntity == null) {
            return new TeamResponseDto(null, null, List.of());
        }

        Member member = MemberMapper.toDomain(memberEntity);
        UUID teamId = member.getTeamId();

        TeamEntity teamEntity = teamRepository.findById(teamId).orElse(null);

        List<MemberEntity> memberEntities = memberRepository.findAllByTeamEntity_Id(teamId);

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
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));
        String teamCode = UUID.randomUUID().toString().substring(0, 8);

        TeamEntity teamEntity = TeamEntity.builder()
                .name(createTeamRequestDto.teamName())
                .code(teamCode)
                .build();

        teamRepository.save(teamEntity);

        MemberEntity memberEntity = new MemberEntity(
                null,
                user,
                teamEntity,
                MemberRole.NONE
        );
        memberRepository.save(memberEntity);

        List<MemberEntity> members = memberRepository.findAllByTeamEntity_Id(teamEntity.getId());
        List<TeamResponseDto.MemberDto> memberList = members.stream()
                .map(MemberMapper::toDto)
                .toList();

        ErdEntity erdEntity = ErdEntity.builder()
                .name(createTeamRequestDto.teamName() + "의 ERD")
                .teamEntity(teamEntity)
                .build();

        teamEntity.setErd(erdEntity);

        return new TeamResponseDto(teamEntity.getName(), teamEntity.getCode(), memberList);
    }

    @Override
    public TeamResponseDto joinTeam(UUID userId, String teamCode) {
        TeamEntity teamEntity = teamRepository.findByCode(teamCode)
                .orElse(null);

        if (teamEntity == null) return null;

        if (memberRepository.findByUserEntity_Id(userId).isPresent()) {
            throw new CustomException(ErrorCode.CONFLICT_TEAM_BUILDING);
        }

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));

        MemberEntity memberEntity = new MemberEntity(
                null,
                user,
                teamEntity,
                MemberRole.NONE
        );
        memberRepository.save(memberEntity);

        List<MemberEntity> memberEntities = memberRepository.findAllByTeamEntity_Id(teamEntity.getId());

        List<TeamResponseDto.MemberDto> memberDtos = memberEntities.stream()
                .map(MemberMapper::toDto)
                .toList();

        return new TeamResponseDto(teamEntity.getName(), teamCode, memberDtos);
    }

    @Override
    public void leaveTeam(UUID userId) {
        MemberEntity member = memberRepository.findByUserEntity_Id(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        memberRepository.delete(member);
    }
}
