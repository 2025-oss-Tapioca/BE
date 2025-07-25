package com.tapioca.BE.domain.port.in.usecase.erd;

import com.tapioca.BE.application.command.erd.UpdateDiagramsCommand;
import com.tapioca.BE.application.dto.response.erd.*;

public interface ErdUseCase {
    ErdResponseDto updateDiagrams(UpdateDiagramsCommand cmd);
}
