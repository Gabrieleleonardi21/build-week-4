package com.example.buildweek4.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateFatturaDTO(
        @NotNull(message = "La data e' obbligatoria")
        LocalDate data,
        // @NotNull e non @NotBlank: @NotBlank vale solo per le stringhe e su un
        // BigDecimal farebbe fallire la validazione con UnexpectedTypeException
        @NotNull(message = "L'importo deve essere valido")
        BigDecimal importo,
        @NotBlank(message = "Il numero deve essere valido")
        String numero
) {
}
