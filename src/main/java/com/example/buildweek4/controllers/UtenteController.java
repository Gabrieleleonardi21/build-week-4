package com.example.buildweek4.controllers;

import com.example.buildweek4.dto.CambioRuoloDTO;
import com.example.buildweek4.dto.RegisterRequestDTO;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.services.UtenteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente register(@RequestBody @Validated RegisterRequestDTO dto) {
        return utenteService.register(dto);
    }

    // GET /utenti -> solo ADMIN (gli serve per trovare gli id da promuovere)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Utente> findAll() {
        return utenteService.findAll();
    }

    // PATCH /utenti/{id}/ruolo -> solo ADMIN: promuove un utente a COMMERCIALE, CONTABILE o ADMIN
    @PatchMapping("/{id}/ruolo")
    @PreAuthorize("hasRole('ADMIN')")
    public Utente cambiaRuolo(@PathVariable UUID id, @RequestBody @Validated CambioRuoloDTO dto) {
        return utenteService.cambiaRuolo(id, dto.ruolo());
    }
}
