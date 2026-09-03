package com.example.buildweek4.services;

import com.example.buildweek4.dto.NewStatoFatturaDTO;
import com.example.buildweek4.entities.Ruolo;
import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.exceptions.BadRequestException;
import com.example.buildweek4.exceptions.ForbiddenException;
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

    // lo stato riservato all'ADMIN: usato anche da FatturaService per la transizione
    public static final String INSOLUTA = "INSOLUTA";

    private final StatoFatturaRepository statoFatturaRepository;
    private final FatturaRepository fatturaRepository;

    public List<StatoFattura> findAll() {
        return statoFatturaRepository.findAll();
    }

    public StatoFattura getById(UUID id) {
        return statoFatturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("StatoFattura con id " + id + " non trovato"));
    }

    public StatoFattura save(NewStatoFatturaDTO body, Utente currentUser) {
        vietaSeInsoluta(body.nome(), currentUser);
        if (statoFatturaRepository.existsByNome(body.nome())) {
            throw new BadRequestException("Stato fattura esistente: " + body.nome());
        }
        StatoFattura stato = new StatoFattura(body.nome());
        stato.setDataCreazione(LocalDateTime.now());
        stato.setDataModifica(LocalDateTime.now());
        return statoFatturaRepository.save(stato);
    }

    public StatoFattura update(UUID id, NewStatoFatturaDTO body, Utente currentUser) {
        StatoFattura stato = this.getById(id);
        // controllati entrambi i nomi: senza il primo un contabile potrebbe rinominare
        // SCADUTA in INSOLUTA e portarci tutte le fatture collegate, senza il secondo
        // potrebbe svuotare INSOLUTA rinominandola in qualcos'altro
        vietaSeInsoluta(body.nome(), currentUser);
        vietaSeInsoluta(stato.getNome(), currentUser);
        if (!stato.getNome().equals(body.nome()) && statoFatturaRepository.existsByNome(body.nome())) {
            throw new BadRequestException("Stato fattura esistente: " + body.nome());
        }
        stato.setNome(body.nome());
        stato.setDataModifica(LocalDateTime.now());
        return statoFatturaRepository.save(stato);
    }

    public void delete(UUID id, Utente currentUser) {
        StatoFattura stato = this.getById(id);
        vietaSeInsoluta(stato.getNome(), currentUser);
        // uno stato ancora referenziato da una fattura non si puo' cancellare:
        // lascerebbe la fattura con un riferimento rotto
        if (fatturaRepository.existsByStatoId(id)) {
            throw new BadRequestException("Stato in uso, non eliminabile");
        }
        statoFatturaRepository.delete(stato);
    }

    // il contabile gestisce liberamente gli stati, tranne INSOLUTA: quello resta
    // all'ADMIN, altrimenti il vincolo sulla transizione delle fatture si aggira
    // semplicemente rinominando gli stati
    private void vietaSeInsoluta(String nome, Utente currentUser) {
        if (INSOLUTA.equalsIgnoreCase(nome) && currentUser.getRuolo() != Ruolo.ADMIN) {
            throw new ForbiddenException("Solo un ADMIN puo' gestire lo stato " + INSOLUTA);
        }
    }
}
