package com.tapioca.BE.application.service.team;

import com.tapioca.BE.adapter.out.entity.MemberEntity;
import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.adapter.out.entity.UserEntity;
import com.tapioca.BE.adapter.out.jpaRepository.MemberJpaRepository;
import com.tapioca.BE.adapter.out.jpaRepository.TeamJpaRepository;
import com.tapioca.BE.adapter.out.jpaRepository.UserJpaRepository;
import com.tapioca.BE.adapter.out.mapper.MemberMapper;
import com.tapioca.BE.application.dto.request.team.CreateTeamRequestDto;
import com.tapioca.BE.application.dto.response.team.TeamResponseDto;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.Member;
import com.tapioca.BE.domain.port.in.usecase.team.TeamUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService implements TeamUseCase {
    private final TeamJpaRepository teamRepository;
    private final UserJpaRepository userRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    public TeamResponseDto getTeamInfo(UUID userId) {
        MemberEntity memberEntity = memberRepository.findByUserEntity_Id(userId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));

        if (memberEntity == null) {
            return new TeamResponseDto(null, List.of());
        }

        Member member = MemberMapper.toDomain(memberEntity);
        UUID teamId = member.getTeamId();

        TeamEntity teamEntity = teamRepository.findById(teamId).orElse(null);
        List<MemberEntity> memberEntities = memberRepository.findAllByTeamEntity_Id(teamId);

        List<TeamResponseDto.MemberDto> memberDtoList = memberEntities.stream().map(me -> {
            String name = me.getUserEntity().getName();
            String role = me.getMemberRole();
            return new TeamResponseDto.MemberDto(name, role);
        }).toList();

        return new TeamResponseDto(teamEntity.getName(), memberDtoList);
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
                "NONE"
        );
        memberRepository.save(memberEntity);

        List<MemberEntity> members = memberRepository.findAllByTeamEntity_Id(teamEntity.getId());
        List<TeamResponseDto.MemberDto> memberList = members.stream()
                .map(member -> new TeamResponseDto.MemberDto(
                        member.getUserEntity().getName(),
                        member.getMemberRole()
                ))
                .toList();

        return new TeamResponseDto(teamEntity.getName(), memberList);
    }
}
