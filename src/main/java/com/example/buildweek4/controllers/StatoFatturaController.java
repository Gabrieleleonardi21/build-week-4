package com.example.buildweek4.controllers;

import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.payload.NewStatoFatturaDTO;
import com.example.buildweek4.services.StatoFatturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/stati-fattura", "/api/stato_fatture"})
@RequiredArgsConstructor
public class StatoFatturaController {
    private final StatoFatturaService statoFatturaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StatoFattura create(@RequestBody @Validated NewStatoFatturaDTO body) {
        return statoFatturaService.save(body);
    }

    @GetMapping
    public List<StatoFattura> findAll() {
        return statoFatturaService.findAll();
    }

    @GetMapping("/{id}")
    public StatoFattura getById(@PathVariable UUID id) {
        return statoFatturaService.getById(id);
    }

    @PutMapping("/{id}")
    public StatoFattura update(@PathVariable UUID id, @RequestBody @Validated NewStatoFatturaDTO body) {
        return statoFatturaService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        statoFatturaService.delete(id);
    }
}
