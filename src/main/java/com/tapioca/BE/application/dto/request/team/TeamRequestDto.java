package com.tapioca.BE.application.dto.request.team;

import jakarta.validation.constraints.NotBlank;

public record TeamRequestDto(
        @NotBlank String teamCode
) {
}
