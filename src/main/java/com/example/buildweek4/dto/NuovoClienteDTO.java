package com.example.buildweek4.dto;

import com.example.buildweek4.entities.TipoSocietario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.UUID;

public record NuovoClienteDTO(
        @NotBlank String ragioneSociale,
        @NotBlank String partitaIva,
        @Email @NotBlank String email,
        BigDecimal fatturatoAnnuale,
        TipoSocietario tipo,
        String logoAziendale,
        UUID sedeLegaleId,
        UUID sedeOperativaId
) {}
