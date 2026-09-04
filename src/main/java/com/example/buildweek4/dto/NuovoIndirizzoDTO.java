package com.example.buildweek4.dto;

import jakarta.validation.constraints.NotBlank;

public record NuovoIndirizzoDTO(
        @NotBlank(message = "La via è obbligatoria")
        String via,
        @NotBlank(message = "Il civico è obbligatorio")
        String civico,
        @NotBlank(message = "La città è obbligatoria")
        String citta,
        @NotBlank(message = "La provincia è obbligatoria")
        String provincia,
        @NotBlank(message = "Il CAP è obbligatorio")
        String cap
) {
}
