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
    // factory statico: tiene la conversione Nota -> DTO in un unico punto,
    // cosi' il controller non la ripete uguale in ognuno dei suoi 4 endpoint
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
