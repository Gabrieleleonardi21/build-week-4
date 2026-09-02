package com.example.buildweek4.dto;

import jakarta.validation.constraints.NotBlank;

public record TransizioneStatoDTO(
        @NotBlank(message = "Lo stato e' obbligatorio")
        String nuovoStato) {
}
