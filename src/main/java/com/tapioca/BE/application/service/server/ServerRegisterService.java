package com.tapioca.BE.application.service.server;

import com.tapioca.BE.adapter.out.entity.BackEntity;
import com.tapioca.BE.adapter.out.mapper.BackMapper;
import com.tapioca.BE.application.dto.request.server.RegisterRequestDto;
import com.tapioca.BE.domain.model.BackEnd;
import com.tapioca.BE.domain.port.in.usecase.server.ServerRegisterUseCase;
import com.tapioca.BE.domain.port.out.repository.server.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ServerRegisterService implements ServerRegisterUseCase {

    private final ServerRepository serverRepository;
    private final BackMapper backMapper;

    @Override
    public void register(RegisterRequestDto dto) {
        // 이미 등록된 서버인지 확인

        BackEnd backend = backMapper.toDomain(dto);

        BackEntity mappingBackEntity = backMapper.toEntity(backend);
        serverRepository.save(mappingBackEntity);
    }
}
