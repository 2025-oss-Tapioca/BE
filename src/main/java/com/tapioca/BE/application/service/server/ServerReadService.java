package com.tapioca.BE.application.service.server;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.entity.project.DbEntity;
import com.tapioca.BE.adapter.out.entity.project.FrontEntity;
import com.tapioca.BE.adapter.out.mapper.BackEndMapper;
import com.tapioca.BE.adapter.out.mapper.DbMapper;
import com.tapioca.BE.adapter.out.mapper.FrontMapper;
import com.tapioca.BE.adapter.out.mapper.ServerMapper;
import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;
import com.tapioca.BE.application.dto.response.server.ReadServerResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.project.BackEnd;
import com.tapioca.BE.domain.model.project.DB;
import com.tapioca.BE.domain.model.project.Front;
import com.tapioca.BE.domain.port.in.usecase.server.ServerReadUseCase;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import com.tapioca.BE.domain.port.out.repository.db.DbRepository;
import com.tapioca.BE.domain.port.out.repository.front.FrontRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import com.tapioca.BE.domain.port.out.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ServerReadService implements ServerReadUseCase {

    private final FrontRepository frontRepository;
    private final BackRepository backRepository;
    private final DbRepository dbRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    private final FrontMapper frontMapper;
    private final BackEndMapper backEndMapper;
    private final DbMapper dbMapper;

    private final ServerMapper serverMapper;

    @Override
    public ReadServerResponseDto read(UUID userId, String teamCode) {

        // 존재하는 팀인지 확인
        teamRepository.findByTeamCode(teamCode);

        // user가 해당 팀의 멤버인지 확인
        if (!memberRepository.existsByUserIdAndTeamCode(userId, teamCode)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED_MEMBER);
        }

        // Entity 조회
        FrontEntity fe = frontRepository.findByCode(teamCode).orElse(null);
        BackEntity be = backRepository.findByTeamCode(teamCode).orElse(null);
        DbEntity de = dbRepository.findByTeamCode(teamCode).orElse(null);

        // domain 변환
        Front f = fe != null ? frontMapper.toDomain(fe) : null;
        BackEnd b = be != null ? backEndMapper.toDomain(be) : null;
        DB d = de != null ? dbMapper.toDomain(de) : null;

        return new ReadServerResponseDto(
                teamCode,
                serverMapper.toFrontResponse(f),
                serverMapper.toBackResponse(b),
                serverMapper.toDbResponse(d)
        );
    }
}
