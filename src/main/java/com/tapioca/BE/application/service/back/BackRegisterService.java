package com.tapioca.BE.application.service.back;

import com.tapioca.BE.adapter.out.entity.BackEntity;
import com.tapioca.BE.adapter.out.entity.MemberEntity;
import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.adapter.out.mapper.BackEndMapper;
import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;
import com.tapioca.BE.domain.model.BackEnd;
import com.tapioca.BE.domain.port.in.usecase.back.BackRegisterUseCase;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import com.tapioca.BE.domain.port.out.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BackRegisterService implements BackRegisterUseCase {

    private final BackRepository backRepository;
    private final BackEndMapper backMapper;
    private final TeamRepository teamRepository;

    @Override
    public void register(RegisterRequestDto dto) {
        // 이미 등록된 서버인지 확인

        // dto의 teamId로 teamEntity 생성

        // TeamEntity teamEntity = teamRepository.findbyTeamId();
        TeamEntity teamEntity;

        BackEnd backend = backMapper.toDomain(dto);

        BackEntity mappingBackEntity = backMapper.toEntity(backend, teamEntity);
        backRepository.save(mappingBackEntity);
    }
}
