package com.tapioca.BE.application.service.erd;

import com.tapioca.BE.application.command.erd.CreateDiagramCommand;
import com.tapioca.BE.application.dto.response.erd.ErdResponseDto;
import com.tapioca.BE.domain.port.in.usecase.erd.ErdUseCase;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ErdService implements ErdUseCase {

    @Override
    @Transactional
    public ErdResponseDto createDiagram(CreateDiagramCommand command) {

    }
}
