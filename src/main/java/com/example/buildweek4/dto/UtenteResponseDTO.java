package com.example.buildweek4.dto;

import com.example.buildweek4.entities.Ruolo;
import com.example.buildweek4.entities.Utente;

import java.time.LocalDateTime;
import java.util.UUID;

public record UtenteResponseDTO(
        UUID id,
        String email,
        String nome,
        String cognome,
        Ruolo ruolo,
        LocalDateTime dataCreazione,
        LocalDateTime dataModifica
) {
    // factory statico: tiene la conversione Utente -> DTO in un unico punto.
    // Utente implementa UserDetails per Spring Security (authorities, accountNonExpired,
    // credentialsNonExpired, enabled, username): sono dettagli interni dell'autenticazione,
    // non hanno senso nella risposta di un'API rivolta al client
    public static UtenteResponseDTO from(Utente utente) {
        return new UtenteResponseDTO(
                utente.getId(),
                utente.getEmail(),
                utente.getNome(),
                utente.getCognome(),
                utente.getRuolo(),
                utente.getDataCreazione(),
                utente.getDataModifica()
        );
    }
}
