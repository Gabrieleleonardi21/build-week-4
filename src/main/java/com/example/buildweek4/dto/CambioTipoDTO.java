package com.example.buildweek4.dto;

import jakarta.validation.constraints.NotBlank;

// il tipo arriva come stringa e viene convertito in enum dal service,
// cosi' un valore non valido produce un 400 con l'elenco dei tipi ammessi
public record CambioTipoDTO(
        @NotBlank String tipo
) {}
