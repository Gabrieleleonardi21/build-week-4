package com.example.buildweek4.dto;

import com.example.buildweek4.entities.TipoSocietario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

// le sedi arrivano come id e non come oggetto Indirizzo: il service le risolve
// dal repository, cosi' l'entita' assegnata al cliente e' gestita da Hibernate
public record ModificaClienteDTO(

        @NotBlank(message = "La ragione sociale è obbligatoria")
        String ragioneSociale,
        @NotBlank(message = "La partita IVA è obbligatoria")
        String partitaIva,
        @NotBlank(message = "L'email è obbligatoria")
        String email,
        @NotNull(message = "Il fatturato annuale è obbligatorio")
        BigDecimal fatturatoAnnuale,
        @NotNull(message = "Il tipo societario è obbligatorio")
        TipoSocietario tipo,
        @NotBlank(message = "Il logo aziendale è obbligatorio")
        String logoAziendale,
        @NotNull(message = "La sede legale è obbligatoria")
        UUID sedeLegaleId,
        @NotNull(message = "La sede operativa è obbligatoria")
        UUID sedeOperativaId) {

}
