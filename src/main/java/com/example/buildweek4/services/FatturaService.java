package com.example.buildweek4.services;

import com.example.buildweek4.dto.NewFatturaDTO;
import com.example.buildweek4.dto.UpdateFatturaDTO;
import com.example.buildweek4.entities.Cliente;
import com.example.buildweek4.entities.Fattura;
import com.example.buildweek4.entities.Ruolo;
import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.exceptions.ForbiddenException;
import com.example.buildweek4.exceptions.NotFoundException;
import com.example.buildweek4.repositories.ClienteRepository;
import com.example.buildweek4.repositories.FatturaRepository;
import com.example.buildweek4.repositories.StatoFatturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FatturaService {
    private final FatturaRepository fatturaRepository;
    private final ClienteRepository clienteRepository;
    private final StatoFatturaRepository statoFatturaRepository;

    public Page<Fattura> getAll(Pageable pageable) {
        return fatturaRepository.findAll(pageable);
    }

    public Fattura getById(UUID id) {
        return fatturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fattura con id " + id + " non trovata"));
    }

    // clienteId e statoId sono opzionali: se null la query ignora quel filtro
    public Page<Fattura> filtra(UUID clienteId, UUID statoId, Pageable pageable) {
        return fatturaRepository.filtra(clienteId, statoId, pageable);
    }

    public Fattura save(NewFatturaDTO body) {
        Fattura fattura = new Fattura();
        fattura.setData(body.data());
        fattura.setImporto(body.importo());
        fattura.setNumero(body.numero());
        Cliente cliente = clienteRepository.findById(body.clienteId())
                .orElseThrow(() -> new NotFoundException("Cliente con id " + body.clienteId() + " non trovato"));
        StatoFattura bozza = statoFatturaRepository.findByNome("BOZZA")
                .orElseThrow(() -> new NotFoundException("Stato BOZZA non presente a database"));
        fattura.setCliente(cliente);
        fattura.setStato(bozza);
        fattura.setDataCreazione(LocalDateTime.now());
        fattura.setDataModifica(LocalDateTime.now());

        return fatturaRepository.save(fattura);
    }

    public Fattura cambiaStato(UUID fatturaId, String nuovoStatoNome, Utente currentUser) {
        Fattura fattura = this.getById(fatturaId);
        // normalizzato prima di ogni controllo: gli stati sono salvati in maiuscolo
        // (vedi DataSeeder), senza questo un client che manda "emessa" in minuscolo
        // non troverebbe lo stato e si vedrebbe rifiutare una richiesta legittima
        String statoRichiesto = nuovoStatoNome.toUpperCase();
        // nessun percorso obbligato fra gli stati: da qualsiasi stato si puo'
        // passare a qualsiasi altro, l'unico limite e' l'autorizzazione.
        // A filtrare i nomi ammessi basta la ricerca qui sotto, che trova solo
        // gli stati esistenti a database
        // il controllo sta qui e non in un @PreAuthorize perche' dipende dal contenuto
        // del body: l'endpoint resta aperto ai contabili, e' solo questo stato a essere riservato
        if (StatoFatturaService.INSOLUTA.equals(statoRichiesto) && currentUser.getRuolo() != Ruolo.ADMIN) {
            throw new ForbiddenException("Solo un ADMIN puo' portare una fattura in stato " + StatoFatturaService.INSOLUTA);
        }
        StatoFattura nuovoStato = statoFatturaRepository.findByNome(statoRichiesto)
                .orElseThrow(() -> new NotFoundException("Stato non trovato: " + statoRichiesto));
        fattura.setStato(nuovoStato);
        fattura.setDataModifica(LocalDateTime.now());
        return fatturaRepository.save(fattura);
    }

    public Fattura update(UUID fatturaId, UpdateFatturaDTO body) {
        Fattura fattura = this.getById(fatturaId);
        fattura.setData(body.data());
        fattura.setImporto(body.importo());
        fattura.setNumero(body.numero());
        fattura.setDataModifica(LocalDateTime.now());
        return fatturaRepository.save(fattura);
    }

    // nessun vincolo da controllare: nessuna entita' referenzia la fattura
    public void delete(UUID fatturaId) {
        Fattura fattura = this.getById(fatturaId);
        fatturaRepository.delete(fattura);
    }
}
