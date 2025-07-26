package com.tapioca.BE.application.service.front;

import com.tapioca.BE.adapter.out.entity.FrontEntity;
import com.tapioca.BE.adapter.out.mapper.FrontMapper;
import com.tapioca.BE.application.dto.request.front.RegisterRequestDto;
import com.tapioca.BE.domain.model.Front;
import com.tapioca.BE.domain.port.in.usecase.front.FrontRegisterUseCase;
import com.tapioca.BE.domain.port.out.repository.front.FrontRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FrontRegisterService implements FrontRegisterUseCase {

    private final FrontRepository frontRepository;
    private final FrontMapper frontMapper;

    @Override
    public void register(RegisterRequestDto dto) {
        // 이미 등록된 서버인지 확인

        Front front = frontMapper.toDomain(dto);

        FrontEntity mappingFrontEntity = frontMapper.toEntity(front);
        frontRepository.save(mappingFrontEntity);
    }
}
