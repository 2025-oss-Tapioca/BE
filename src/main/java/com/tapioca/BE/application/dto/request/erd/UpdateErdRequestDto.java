package com.tapioca.BE.application.dto.request.erd;

import com.tapioca.BE.application.command.erd.UpdateDiagramsCommand;
import com.tapioca.BE.application.command.erd.UpdateDiagramsCommand.*;
import com.tapioca.BE.domain.model.type.AttributeType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record UpdateErdRequestDto(
        String name,
        List<UpdateDiagramRequestDto> diagrams,
        List<UpdateAttributeLinkRequestDto> attributes
) {}
