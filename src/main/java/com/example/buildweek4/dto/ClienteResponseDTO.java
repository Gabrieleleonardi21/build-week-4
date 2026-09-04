package com.example.buildweek4.dto;

import com.example.buildweek4.entities.Cliente;
import com.example.buildweek4.entities.Indirizzo;
import com.example.buildweek4.entities.TipoSocietario;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteResponseDTO(
        UUID id,
        String ragioneSociale,
        String partitaIva,
        String email,
        BigDecimal fatturatoAnnuale,
        TipoSocietario tipo,
        String logoAziendale,
        Indirizzo sedeLegale,
        Indirizzo sedeOperativa,
        UtenteResponseDTO commerciale,
        LocalDateTime dataCreazione,
        LocalDateTime dataModifica
) {
    // factory statico: tiene la conversione Cliente -> DTO in un unico punto.
    // il commerciale passa da UtenteResponseDTO.from(...) invece dell'Utente grezzo,
    // altrimenti si riporterebbe dentro tutto il rumore di UserDetails (authorities,
    // accountNonExpired, ecc.) che avevamo gia' tolto da Nota e Fattura.
    // commerciale e' facoltativo (un cliente creato dall'ADMIN puo' non averne uno)
    public static ClienteResponseDTO from(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getRagioneSociale(),
                cliente.getPartitaIva(),
                cliente.getEmail(),
                cliente.getFatturatoAnnuale(),
                cliente.getTipo(),
                cliente.getLogoAziendale(),
                cliente.getSedeLegale(),
                cliente.getSedeOperativa(),
                cliente.getCommerciale() != null ? UtenteResponseDTO.from(cliente.getCommerciale()) : null,
                cliente.getDataCreazione(),
                cliente.getDataModifica()
        );
    }
}
