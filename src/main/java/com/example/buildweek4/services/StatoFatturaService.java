package com.example.buildweek4.services;

import com.example.buildweek4.dto.NewStatoFatturaDTO;
import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.exceptions.BadRequestException;
import com.example.buildweek4.exceptions.NotFoundException;
import com.example.buildweek4.repositories.FatturaRepository;
import com.example.buildweek4.repositories.StatoFatturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatoFatturaService {

    private final StatoFatturaRepository statoFatturaRepository;
    private final FatturaRepository fatturaRepository;

    public List<StatoFattura> findAll() {
        return statoFatturaRepository.findAll();
    }

    public StatoFattura getById(UUID id) {
        return statoFatturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("StatoFattura con id " + id + " non trovato"));
    }

    public StatoFattura save(NewStatoFatturaDTO body) {
        if (statoFatturaRepository.existsByNome(body.nome())) {
            throw new BadRequestException("Stato fattura esistente: " + body.nome());
        }
        StatoFattura stato = new StatoFattura(body.nome());
        stato.setDataCreazione(LocalDateTime.now());
        stato.setDataModifica(LocalDateTime.now());
        return statoFatturaRepository.save(stato);
    }

    public StatoFattura update(UUID id, NewStatoFatturaDTO body) {
        StatoFattura stato = this.getById(id);
        if (!stato.getNome().equals(body.nome()) && statoFatturaRepository.existsByNome(body.nome())) {
            throw new BadRequestException("Stato fattura esistente: " + body.nome());
        }
        stato.setNome(body.nome());
        stato.setDataModifica(LocalDateTime.now());
        return statoFatturaRepository.save(stato);
    }

    public void delete(UUID id) {
        StatoFattura stato = this.getById(id);
        // uno stato ancora referenziato da una fattura non si puo' cancellare:
        // lascerebbe la fattura con un riferimento rotto
        if (fatturaRepository.existsByStatoId(id)) {
            throw new BadRequestException("Stato in uso, non eliminabile");
        }
        statoFatturaRepository.delete(stato);
    }
}
