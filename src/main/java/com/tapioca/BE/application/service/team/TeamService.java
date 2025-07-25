package com.tapioca.BE.application.service.team;

import com.tapioca.BE.adapter.out.entity.MemberEntity;
import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.adapter.out.jpaRepository.MemberJpaRepository;
import com.tapioca.BE.adapter.out.jpaRepository.TeamJpaRepository;
import com.tapioca.BE.adapter.out.jpaRepository.UserJpaRepository;
import com.tapioca.BE.adapter.out.mapper.MemberMapper;
import com.tapioca.BE.application.dto.response.team.TeamResponseDto;
import com.tapioca.BE.domain.model.Member;
import com.tapioca.BE.domain.port.in.usecase.team.TeamUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        MemberEntity memberEntity = memberRepository.findByUserEntity_Id(userId).orElse(null);

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
}
