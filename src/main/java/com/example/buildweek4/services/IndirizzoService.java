package com.example.buildweek4.services;


import com.example.buildweek4.dto.NuovoIndirizzoDTO;
import com.example.buildweek4.dto.PatchIndirizzoDTO;
import com.example.buildweek4.entities.Indirizzo;
import com.example.buildweek4.exceptions.NotFoundException;
import com.example.buildweek4.repositories.IndirizzoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class IndirizzoService {

    private final IndirizzoRepository indirizzoRepository;

    public IndirizzoService(IndirizzoRepository indirizzoRepository) {
        this.indirizzoRepository = indirizzoRepository;
    }

    public List<Indirizzo> getAll() {
        return indirizzoRepository.findAll();
    }

    public Indirizzo getById(UUID indirizzoId) {
        return indirizzoRepository.findById(indirizzoId).orElseThrow(() -> new NotFoundException("Indirizzo con id " + indirizzoId + " non trovato"));
    }

    public Indirizzo save(NuovoIndirizzoDTO dto) {
        Indirizzo indirizzo = new Indirizzo(dto.via(), dto.civico(), dto.citta(), dto.provincia(), dto.cap());
        indirizzo.setDataCreazione(LocalDateTime.now());
        indirizzo.setDataModifica(LocalDateTime.now());
        return indirizzoRepository.save(indirizzo);
    }

    public Indirizzo modificaIndirizzo(UUID indirizzoCorrenteId, NuovoIndirizzoDTO dto) {
        Indirizzo indirizzo = getById(indirizzoCorrenteId);

        indirizzo.setVia(dto.via());
        indirizzo.setCivico(dto.civico());
        indirizzo.setCitta(dto.citta());
        indirizzo.setProvincia(dto.provincia());
        indirizzo.setCap(dto.cap());
        indirizzo.setDataModifica(LocalDateTime.now());

        return indirizzoRepository.save(indirizzo);
    }

    public Indirizzo patchIndirizzo (UUID indirizzoCorrenteId, PatchIndirizzoDTO dto) {
        Indirizzo indirizzo = getById(indirizzoCorrenteId);

        if (dto.via() != null) indirizzo.setVia(dto.via());
        if (dto.civico() != null) indirizzo.setCivico(dto.civico());
        if (dto.citta() != null) indirizzo.setCitta(dto.citta());
        if (dto.provincia() != null) indirizzo.setProvincia(dto.provincia());
        if (dto.cap() != null) indirizzo.setCap(dto.cap());
        indirizzo.setDataModifica(LocalDateTime.now());

        return indirizzoRepository.save(indirizzo);
    }
}
