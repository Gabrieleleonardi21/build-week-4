package com.example.buildweek4.dto;

import jakarta.validation.constraints.NotBlank;

public record NewStatoFatturaDTO(
        @NotBlank(message = "Il nome e' obbligatorio")
        String nome
) { }
