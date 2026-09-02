package com.example.buildweek4.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record NewFatturaDTO(
        @NotNull(message = "La data e' obbligatoria")
        LocalDate data,
        @NotNull(message = "Inserire un importo valido")
        BigDecimal importo,
        @NotBlank(message = "Inserire un numero valido")
        String numero,
        @NotNull(message = "Inserire un Id valido")
        UUID clienteId
) { }
