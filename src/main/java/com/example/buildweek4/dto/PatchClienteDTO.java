package com.example.buildweek4.dto;

import com.example.buildweek4.entities.TipoSocietario;
import jakarta.validation.constraints.Email;

import java.math.BigDecimal;
import java.util.UUID;

// record e non classe: Jackson popola i campi tramite il costruttore canonico.
// Con la vecchia classe @Getter (nessun setter, campi privati) arrivavano tutti null.
// Nessun @NotBlank: in una PATCH i campi assenti restano null e vengono ignorati,
// quindi obbligarli avrebbe reso impossibile modificarne uno solo per volta.
public record PatchClienteDTO(
        String ragioneSociale,
        String partitaIva,
        @Email(message = "L'email deve essere valida")
        String email,
        BigDecimal fatturatoAnnuale,
        TipoSocietario tipo,
        String logoAziendale,
        UUID sedeLegaleId,
        UUID sedeOperativaId
) {}
