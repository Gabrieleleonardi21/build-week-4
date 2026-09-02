package com.example.buildweek4.services;

import com.example.buildweek4.entities.Fattura;
import com.example.buildweek4.exceptions.NotFoundException;
import com.example.buildweek4.repositories.FatturaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class FatturaService {

    private final FatturaRepository fatturaRepository;

    public FatturaService(FatturaRepository fatturaRepository) {
        this.fatturaRepository = fatturaRepository;
    }

    public Page<Fattura> getAll(Pageable pageable) {
        return fatturaRepository.findAll(pageable);
    }

    public Fattura getById(UUID id) {
        return fatturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fattura con id " + id + " non trovata"));
    }
}
