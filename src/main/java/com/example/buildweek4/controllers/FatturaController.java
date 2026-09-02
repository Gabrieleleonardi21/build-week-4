package com.example.buildweek4.controllers;

import com.example.buildweek4.entities.Fattura;
import com.example.buildweek4.payload.NewFatturaDTO;
import com.example.buildweek4.payload.TransizioneStatoDTO;
import com.example.buildweek4.payload.UpdateFatturaDTO;
import com.example.buildweek4.services.FatturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/fatture", "/api/fatture"})
@RequiredArgsConstructor
public class FatturaController {
    private final FatturaService fatturaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fattura create(@RequestBody @Validated NewFatturaDTO body) {
        return fatturaService.save(body);
    }

    @PatchMapping("/{id}/stato")
    public Fattura cambiaStato(@PathVariable UUID id, @RequestBody @Validated TransizioneStatoDTO body) {
        return fatturaService.cambiaStato(id, body.nuovoStato());
    }

    @PutMapping("/{id}")
    public Fattura update(@PathVariable UUID id, @RequestBody @Validated UpdateFatturaDTO body) {
        return fatturaService.update(id, body);
    }

    @GetMapping
    public List<Fattura> getFatture(@RequestParam(required = false) UUID clienteId, @RequestParam(required = false) UUID statoId) {
        return fatturaService.filtra(clienteId, statoId);
    }

    @GetMapping("/all")
    public Page<Fattura> getAll(Pageable pageable) {
        return fatturaService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Fattura getById(@PathVariable UUID id) {
        return fatturaService.getById(id);
    }
}
    }
}
