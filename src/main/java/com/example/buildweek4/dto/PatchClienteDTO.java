package com.example.buildweek4.dto;

import com.example.buildweek4.entities.TipoSocietario;

import java.math.BigDecimal;
import java.util.UUID;

public class PatchClienteDTO {
    private String ragioneSociale;
    private String partitaIva;
    private String email;
    private BigDecimal fatturatoAnnuale;
    private TipoSocietario tipo;
    private String logoAziendale;
    private UUID sedeLegaleId;
    private UUID sedeOperativaId;

    public String getRagioneSociale() { return ragioneSociale; }
    public String getPartitaIva() { return partitaIva; }
    public String getEmail() { return email; }
    public BigDecimal getFatturatoAnnuale() { return fatturatoAnnuale; }
    public TipoSocietario getTipo() { return tipo; }
    public String getLogoAziendale() { return logoAziendale; }
    public UUID getSedeLegaleId() { return sedeLegaleId; }
    public UUID getSedeOperativaId() { return sedeOperativaId; }
}
