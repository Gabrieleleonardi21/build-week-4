package com.example.buildweek4.dto;

import com.example.buildweek4.entities.Nota;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotaResponseDTO(
        UUID id,
        String contenuto,
        UUID clienteId,
        String autoreEmail,
        LocalDateTime dataCreazione,
        LocalDateTime dataModifica
) {
    public static NotaResponseDTO from(Nota nota) {
        return new NotaResponseDTO(
                nota.getId(),
                nota.getContenuto(),
                nota.getCliente().getId(),
                nota.getAutore().getEmail(),
                nota.getDataCreazione(),
                nota.getDataModifica()
        );
    }
}
