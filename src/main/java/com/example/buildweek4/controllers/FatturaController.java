package com.example.buildweek4.controllers;

import com.example.buildweek4.entities.Fattura;
import com.example.buildweek4.services.FatturaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/fatture")
public class FatturaController {

    private final FatturaService fatturaService;

    public FatturaController(FatturaService fatturaService) {
        this.fatturaService = fatturaService;
    }

    @GetMapping
    public Page<Fattura> getAll(Pageable pageable) {
        return fatturaService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Fattura getById(@PathVariable UUID id) {
        return fatturaService.getById(id);
    }
}
