package com.tapioca.BE.application.dto.request.team;

import jakarta.validation.constraints.NotBlank;

public record JoinTeamRequestDto(
        @NotBlank String teamCode
) {
}
