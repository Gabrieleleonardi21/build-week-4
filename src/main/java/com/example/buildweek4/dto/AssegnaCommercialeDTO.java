package com.example.buildweek4.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssegnaCommercialeDTO(
        @NotNull(message = "L'id del commerciale e' obbligatorio")
        UUID commercialeId
) {
}
