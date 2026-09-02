package com.example.buildweek4.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotBlank String nome,
        @NotBlank String cognome
) {}
