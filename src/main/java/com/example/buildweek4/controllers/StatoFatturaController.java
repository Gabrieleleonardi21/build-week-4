package com.example.buildweek4.controllers;

import com.example.buildweek4.dto.NewStatoFatturaDTO;
import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.services.StatoFatturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StatoFattura create(@RequestBody @Valid NewStatoFatturaDTO body) {
        return statoFatturaService.save(body);
    }

    @PutMapping("/{id}")
    public StatoFattura update(@PathVariable UUID id, @RequestBody @Valid NewStatoFatturaDTO body) {
        return statoFatturaService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        statoFatturaService.delete(id);
    }
}
