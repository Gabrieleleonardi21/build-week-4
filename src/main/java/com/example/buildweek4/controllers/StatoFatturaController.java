package com.example.buildweek4.controllers;

import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.services.StatoFatturaService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/stato_fatture")
public class StatoFatturaController {

    private final StatoFatturaService statoFatturaService;

    public StatoFatturaController(StatoFatturaService statoFatturaService) {
        this.statoFatturaService = statoFatturaService;
    }

    @GetMapping("/{id}")
    public StatoFattura getById(@PathVariable UUID id) {
        return statoFatturaService.getById(id);
    }
}
