package com.example.buildweek4.dto;

import com.example.buildweek4.entities.TipoSocietario;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class PatchClienteDTO {
    private String ragioneSociale;
    private String partitaIva;
    private String email;
    private BigDecimal fatturatoAnnuale;
    private TipoSocietario tipo;
    private String logoAziendale;
    private UUID sedeLegaleId;
    private UUID sedeOperativaId;
}
