package com.example.buildweek4.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NuovaNotaDTO(
        @NotBlank(message = "Il contenuto della nota e' obbligatorio")
        String contenuto,
        @NotNull(message = "Il cliente e' obbligatorio")
        UUID clienteId
) {
}
