package com.example.buildweek4.services;

import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.exceptions.NotFoundException;
import com.example.buildweek4.repositories.StatoFatturaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StatoFatturaService {

    private final StatoFatturaRepository statoFatturaRepository;

    public StatoFatturaService(StatoFatturaRepository statoFatturaRepository) {
        this.statoFatturaRepository = statoFatturaRepository;
    }

    public StatoFattura getById(UUID id) {
        return statoFatturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("StatoFattura con id " + id + " non trovato"));
    }
}
