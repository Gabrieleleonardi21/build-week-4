package com.example.buildweek4.dto;

import com.example.buildweek4.entities.TipoSocietario;

import java.math.BigDecimal;
import java.util.UUID;

public record PatchClienteDTO(
        String ragioneSociale,
        String partitaIva,
        String email,
        BigDecimal fatturatoAnnuale,
        TipoSocietario tipo,
        String logoAziendale,
        UUID sedeLegaleId,
        UUID sedeOperativaId
) {
}
