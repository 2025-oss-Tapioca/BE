package com.tapioca.BE.domain.port.in.usecase.erd;

import com.tapioca.BE.application.command.erd.CreateDiagramCommand;
import com.tapioca.BE.application.dto.request.erd.*;
import com.tapioca.BE.application.dto.response.erd.*;

public interface ErdUseCase {
    public ErdResponseDto createDiagram(CreateDiagramCommand createDiagramCommand);
}
