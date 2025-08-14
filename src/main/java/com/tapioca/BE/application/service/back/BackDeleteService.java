package com.tapioca.BE.application.service.back;

import com.tapioca.BE.application.dto.request.back.DeleteRequestDto;
import com.tapioca.BE.domain.port.in.usecase.back.BackDeleteUseCase;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BackDeleteService implements BackDeleteUseCase {

    @Override
    public void delete(DeleteRequestDto deleteRequestDto) {


    }
}
