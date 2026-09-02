package com.example.buildweek4.dto;

import jakarta.validation.constraints.NotBlank;

// il ruolo arriva come stringa e viene convertito in enum dal service,
// cosi' un valore non valido produce un 400 con l'elenco dei ruoli ammessi
public record CambioRuoloDTO(
        @NotBlank String ruolo
) {}
