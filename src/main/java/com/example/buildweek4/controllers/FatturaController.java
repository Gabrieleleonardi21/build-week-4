package com.example.buildweek4.controllers;

import com.example.buildweek4.dto.NewFatturaDTO;
import com.example.buildweek4.dto.TransizioneStatoDTO;
import com.example.buildweek4.dto.UpdateFatturaDTO;
import com.example.buildweek4.entities.Fattura;
import com.example.buildweek4.services.FatturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/fatture")
@RequiredArgsConstructor
public class FatturaController {
    private final FatturaService fatturaService;

    // unico endpoint di elenco: i filtri sono opzionali, quindi senza parametri
    // restituisce tutte le fatture paginate, con parametri le filtra
    @GetMapping
    public Page<Fattura> getFatture(@RequestParam(required = false) UUID clienteId,
                                    @RequestParam(required = false) UUID statoId,
                                    Pageable pageable) {
        return fatturaService.filtra(clienteId, statoId, pageable);
    }

    @GetMapping("/{id}")
    public Fattura getById(@PathVariable UUID id) {
        return fatturaService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CONTABILE', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Fattura create(@RequestBody @Valid NewFatturaDTO body) {
        return fatturaService.save(body);
    }

    @PatchMapping("/{id}/stato")
    @PreAuthorize("hasAnyRole('CONTABILE', 'ADMIN')")
    public Fattura cambiaStato(@PathVariable UUID id, @RequestBody @Valid TransizioneStatoDTO body) {
        return fatturaService.cambiaStato(id, body.nuovoStato());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CONTABILE', 'ADMIN')")
    public Fattura update(@PathVariable UUID id, @RequestBody @Valid UpdateFatturaDTO body) {
        return fatturaService.update(id, body);
    }

    // la cancellazione e' l'unica operazione sulle fatture riservata al solo ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        fatturaService.delete(id);
    }
}
