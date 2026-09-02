package com.example.buildweek4.controllers;

import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.payload.NewStatoFatturaDTO;
import com.example.buildweek4.services.StatoFatturaService;
import org.springframework.validation.annotation.Validated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stati-fattura")
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
