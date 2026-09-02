package com.example.buildweek4.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateFatturaDTO(
        @NotNull(message = "La data e' obbligatoria")
        LocalDate data,
        @NotBlank(message = "L'importo deve essere valido")
        BigDecimal importo,
        @NotBlank(message = "Il numero deve essere valido")
        String numero
) {
}
