package com.example.buildweek4.controllers;

import com.example.buildweek4.dto.NewStatoFatturaDTO;
import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.services.StatoFatturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stato_fatture")
@RequiredArgsConstructor
public class StatoFatturaController {
    private final StatoFatturaService statoFatturaService;

    @GetMapping
    public List<StatoFattura> findAll() {
        return statoFatturaService.findAll();
    }

    @GetMapping("/{id}")
    public StatoFattura getById(@PathVariable UUID id) {
        return statoFatturaService.getById(id);
    }

    // il contabile gestisce gli stati, ma il service gli vieta di toccare INSOLUTA
    @PostMapping
    @PreAuthorize("hasAnyRole('CONTABILE', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public StatoFattura create(@RequestBody @Valid NewStatoFatturaDTO body,
                               @AuthenticationPrincipal Utente currentUser) {
        return statoFatturaService.save(body, currentUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CONTABILE', 'ADMIN')")
    public StatoFattura update(@PathVariable UUID id, @RequestBody @Valid NewStatoFatturaDTO body,
                               @AuthenticationPrincipal Utente currentUser) {
        return statoFatturaService.update(id, body, currentUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CONTABILE', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal Utente currentUser) {
        statoFatturaService.delete(id, currentUser);
    }
}
