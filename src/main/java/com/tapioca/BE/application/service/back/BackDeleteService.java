package com.tapioca.BE.application.service.back;

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

    private final BackRepository backRepository;
    private final TeamRepository teamRepository;

    @Override
    public void delete(String teamCode) {


    }
}
