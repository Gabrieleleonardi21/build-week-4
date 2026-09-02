package com.example.buildweek4.controllers;

import com.example.buildweek4.dto.RegisterRequestDTO;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.services.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente register(@RequestBody @Valid RegisterRequestDTO dto) {
        return utenteService.register(dto);
    }
}
