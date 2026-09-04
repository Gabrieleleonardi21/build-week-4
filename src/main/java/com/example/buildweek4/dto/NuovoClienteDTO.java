package com.example.buildweek4.dto;

import com.example.buildweek4.entities.TipoSocietario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.UUID;

// i vincoli restano quelli di prima: cambiano solo i messaggi, che senza un
// message= esplicito uscivano nel testo di default di Bean Validation
// ("non deve essere spazio"), poco chiaro per chi consuma l'API.
// Gli altri campi non hanno vincoli perche' sono facoltativi: il service
// risolve le sedi solo se l'id e' presente nel body
public record NuovoClienteDTO(
        @NotBlank(message = "La ragione sociale è obbligatoria")
        String ragioneSociale,
        @NotBlank(message = "La partita IVA è obbligatoria")
        String partitaIva,
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'email non è in un formato valido")
        String email,
        BigDecimal fatturatoAnnuale,
        TipoSocietario tipo,
        String logoAziendale,
        UUID sedeLegaleId,
        UUID sedeOperativaId
) {}
