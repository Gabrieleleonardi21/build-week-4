package com.example.buildweek4.dto;

import com.example.buildweek4.entities.Fattura;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record FatturaResponseDTO(
        UUID id,
        LocalDate data,
        BigDecimal importo,
        String numero,
        UUID clienteId,
        String clienteRagioneSociale,
        String statoNome,
        LocalDateTime dataCreazione,
        LocalDateTime dataModifica
) {
    // factory statico: tiene la conversione Fattura -> DTO in un unico punto,
    // cosi' il controller non la ripete uguale in ognuno dei suoi 5 endpoint
    public static FatturaResponseDTO from(Fattura fattura) {
        return new FatturaResponseDTO(
                fattura.getId(),
                fattura.getData(),
                fattura.getImporto(),
                fattura.getNumero(),
                fattura.getCliente().getId(),
                fattura.getCliente().getRagioneSociale(),
                fattura.getStato().getNome(),
                fattura.getDataCreazione(),
                fattura.getDataModifica()
        );
    }
}
