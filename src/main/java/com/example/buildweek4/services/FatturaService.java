package com.example.buildweek4.services;

import com.example.buildweek4.entities.Cliente;
import com.example.buildweek4.entities.Fattura;
import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.exceptions.NotFoundException;
import com.example.buildweek4.payload.NewFatturaDTO;
import com.example.buildweek4.payload.UpdateFatturaDTO;
import com.example.buildweek4.repositories.ClienteRepository;
import com.example.buildweek4.repositories.FatturaRepository;
import com.example.buildweek4.repositories.StatoFatturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FatturaService {
    private final FatturaRepository fatturaRepository;
    private final ClienteRepository clienteRepository;
    private final StatoFatturaRepository statoFatturaRepository;

    private static final Map<String, Set<String>> TRANSIZIONI_VALIDE = Map.of(
            "BOZZA", Set.of("EMESSA"),
            "EMESSA", Set.of("PAGATA", "SCADUTA"),
            "SCADUTA", Set.of("INSOLUTA")
    );

    public Fattura save(NewFatturaDTO body) {
        Fattura fattura = new Fattura();
        fattura.setData(body.data());
        fattura.setImporto(body.importo());
        fattura.setNumero(body.numero());

        Cliente cliente = clienteRepository.findById(body.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente non trovato: " + body.clienteId()));
        StatoFattura bozza = statoFatturaRepository.findByNome("BOZZA")
                .orElseThrow(() -> new RuntimeException("Stato non trovato"));
        fattura.setCliente(cliente);
        fattura.setStato(bozza);
        fattura.setDataCreazione(LocalDateTime.now());
        fattura.setDataModifica(LocalDateTime.now());

        return fatturaRepository.save(fattura);
    }

    public Fattura cambiaStato(UUID fatturaId, String nuovoStatoNome) {
        Fattura fattura = fatturaRepository.findById(fatturaId)
                .orElseThrow(() -> new RuntimeException("Fattura non trovata"));

        String statoAttuale = fattura.getStato().getNome();
        Set<String> statiAmmessi = TRANSIZIONI_VALIDE.getOrDefault(statoAttuale, Set.of());
        if (!statiAmmessi.contains(nuovoStatoNome)) {
            throw new RuntimeException("Transizione non valida da " + statoAttuale + " a " + nuovoStatoNome);
        }

        StatoFattura nuovoStato = statoFatturaRepository.findByNome(nuovoStatoNome)
                .orElseThrow(() -> new RuntimeException("Stato non trovato: " + nuovoStatoNome));
        fattura.setStato(nuovoStato);
        fattura.setDataModifica(LocalDateTime.now());
        return fatturaRepository.save(fattura);
    }

    public Fattura update(UUID fatturaId, UpdateFatturaDTO body) {
        Fattura fattura = fatturaRepository.findById(fatturaId)
                .orElseThrow(() -> new RuntimeException("Fattura non trovata"));
        fattura.setData(body.data());
        fattura.setImporto(body.importo());
        fattura.setNumero(body.numero());
        fattura.setDataModifica(LocalDateTime.now());
        return fatturaRepository.save(fattura);
    }

    public List<Fattura> filtra(UUID clienteId, UUID statoId) {
        return fatturaRepository.filtra(clienteId, statoId);
    }

    public Page<Fattura> getAll(Pageable pageable) {
        return fatturaRepository.findAll(pageable);
    }

    public Fattura getById(UUID id) {
        return fatturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fattura con id " + id + " non trovata"));
    }
}
    }
}
