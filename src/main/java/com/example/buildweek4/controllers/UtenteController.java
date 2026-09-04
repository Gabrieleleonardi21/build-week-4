package com.example.buildweek4.controllers;

import com.example.buildweek4.dto.CambioRuoloDTO;
import com.example.buildweek4.dto.RegisterRequestDTO;
import com.example.buildweek4.dto.UtenteResponseDTO;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.services.UtenteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public UtenteResponseDTO register(@RequestBody @Validated RegisterRequestDTO dto) {
        return UtenteResponseDTO.from(utenteService.register(dto));
    }

    // GET /utenti/me -> il proprio profilo: nessun @PreAuthorize, basta essere
    // autenticati. L'Utente arriva dal SecurityContext, dove lo ha messo il
    // JwtFilter partendo dall'id contenuto nel token (la password e' @JsonIgnore)
    @GetMapping("/me")
    public UtenteResponseDTO getProfilo(@AuthenticationPrincipal Utente currentUser) {
        return UtenteResponseDTO.from(currentUser);
    }

    // GET /utenti -> solo ADMIN (gli serve per trovare gli id da promuovere)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UtenteResponseDTO> findAll() {
        return utenteService.findAll().stream()
                .map(UtenteResponseDTO::from)
                .toList();
    }

    // PATCH /utenti/{id}/ruolo -> solo ADMIN: promuove un utente a COMMERCIALE, CONTABILE o ADMIN
    @PatchMapping("/{id}/ruolo")
    @PreAuthorize("hasRole('ADMIN')")
    public UtenteResponseDTO cambiaRuolo(@PathVariable UUID id, @RequestBody @Validated CambioRuoloDTO dto) {
        return UtenteResponseDTO.from(utenteService.cambiaRuolo(id, dto.ruolo()));
    }
}
