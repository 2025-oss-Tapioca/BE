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
import com.tapioca.BE.domain.model.project.BackEnd;
import com.tapioca.BE.domain.model.project.DB;
import com.tapioca.BE.domain.model.project.Front;
import com.tapioca.BE.domain.port.in.usecase.server.ServerReadUseCase;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import com.tapioca.BE.domain.port.out.repository.db.DbRepository;
import com.tapioca.BE.domain.port.out.repository.front.FrontRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ServerReadService implements ServerReadUseCase {

    private final FrontRepository frontRepository;
    private final BackRepository backRepository;
    private final DbRepository dbRepository;

    private final FrontMapper frontMapper;
    private final BackEndMapper backEndMapper;
    private final DbMapper dbMapper;

    private final ServerMapper serverMapper;

    @Override
    public ReadServerResponseDto read(ReadServerRequestDto dto) {

        // teamCode 얻기 위한 임시 도메인 모델
        BackEnd temp = backEndMapper.toDomain(dto);

        // Entity 조회
        FrontEntity fe = frontRepository.findByCode(temp.getTeamCode()).orElse(null);
        BackEntity be = backRepository.findByTeamCode(temp.getTeamCode()).orElse(null);
        DbEntity de = dbRepository.findByTeamCode(temp.getTeamCode()).orElse(null);

        // domain 변환
        Front f = fe != null ? frontMapper.toDomain(fe) : null;
        BackEnd b = be != null ? backEndMapper.toDomain(be) : null;
        DB d = de != null ? dbMapper.toDomain(de) : null;

        return new ReadServerResponseDto(
                temp.getTeamCode(),
                serverMapper.toFrontResponse(f),
                serverMapper.toBackResponse(b),
                serverMapper.toDbResponse(d)
        );
    }
}
