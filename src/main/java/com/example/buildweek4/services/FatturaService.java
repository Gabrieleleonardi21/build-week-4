package com.example.buildweek4.services;

import com.example.buildweek4.dto.NewFatturaDTO;
import com.example.buildweek4.dto.UpdateFatturaDTO;
import com.example.buildweek4.entities.Cliente;
import com.example.buildweek4.entities.Fattura;
import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.exceptions.BadRequestException;
import com.example.buildweek4.exceptions.NotFoundException;
import com.example.buildweek4.repositories.ClienteRepository;
import com.example.buildweek4.repositories.FatturaRepository;
import com.example.buildweek4.repositories.StatoFatturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FatturaService {
    private final FatturaRepository fatturaRepository;
    private final ClienteRepository clienteRepository;
    private final StatoFatturaRepository statoFatturaRepository;

    // transizioni ammesse: da uno stato si puo' passare solo a quelli elencati qui
    private static final Map<String, Set<String>> TRANSIZIONI_VALIDE = Map.of(
            "BOZZA", Set.of("EMESSA"),
            "EMESSA", Set.of("PAGATA", "SCADUTA"),
            "SCADUTA", Set.of("INSOLUTA")
    );

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

    public Fattura cambiaStato(UUID fatturaId, String nuovoStatoNome) {
        Fattura fattura = this.getById(fatturaId);
        String statoAttuale = fattura.getStato().getNome();
        Set<String> statiAmmessi = TRANSIZIONI_VALIDE.getOrDefault(statoAttuale, Set.of());
        if (!statiAmmessi.contains(nuovoStatoNome)) {
            throw new BadRequestException("Transizione non valida da " + statoAttuale + " a " + nuovoStatoNome);
        }
        // TODO: Gestione insoluta da parte di Admin
        StatoFattura nuovoStato = statoFatturaRepository.findByNome(nuovoStatoNome)
                .orElseThrow(() -> new NotFoundException("Stato non trovato: " + nuovoStatoNome));
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
