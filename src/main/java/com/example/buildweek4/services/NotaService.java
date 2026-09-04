package com.example.buildweek4.services;

import com.example.buildweek4.dto.NuovaNotaDTO;
import com.example.buildweek4.dto.PatchNotaDTO;
import com.example.buildweek4.entities.Cliente;
import com.example.buildweek4.entities.Nota;
import com.example.buildweek4.entities.Ruolo;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.exceptions.ForbiddenException;
import com.example.buildweek4.exceptions.NotFoundException;
import com.example.buildweek4.repositories.ClienteRepository;
import com.example.buildweek4.repositories.NotaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotaService {

    private final NotaRepository notaRepository;
    private final ClienteRepository clienteRepository;

    public NotaService(NotaRepository notaRepository, ClienteRepository clienteRepository) {
        this.notaRepository = notaRepository;
        this.clienteRepository = clienteRepository;
    }

    public Nota getById(UUID notaId, Utente currentUser) {
        Nota nota = notaRepository.findById(notaId)
                .orElseThrow(() -> new NotFoundException("Nota con id " + notaId + " non trovata"));

        if (currentUser.getRuolo() == Ruolo.COMMERCIALE) {
            Cliente cliente = nota.getCliente();
            if (cliente.getCommerciale() == null || !cliente.getCommerciale().getId().equals(currentUser.getId())) {
                throw new ForbiddenException("Puoi vedere solo le note dei tuoi clienti");
            }
        }

        return nota;
    }

    public Nota save(NuovaNotaDTO dto, Utente autore) {
        Cliente cliente = clienteRepository.findById(dto.clienteId()).orElseThrow(() -> new NotFoundException("Cliente con id " + dto.clienteId() + " non trovato"));

        // un COMMERCIALE puo' scrivere note solo sui clienti a lui assegnati; l'ADMIN puo' su tutti
        if (autore.getRuolo() == Ruolo.COMMERCIALE && (cliente.getCommerciale() == null || !cliente.getCommerciale().getId().equals(autore.getId()))) {
            throw new ForbiddenException("Puoi aggiungere note solo ai clienti a te assegnati"); // 403
        }

        Nota nota = new Nota(dto.contenuto(), cliente, autore);
        nota.setDataCreazione(LocalDateTime.now());
        nota.setDataModifica(LocalDateTime.now());

        return notaRepository.save(nota);
    }

    public List<Nota> getNote(UUID clienteId, Utente currentUser) {
        if (currentUser.getRuolo() == Ruolo.ADMIN) {
            return clienteId != null
                    ? notaRepository.findByClienteId(clienteId)
                    : notaRepository.findAll();
        }
        List<UUID> clientiAssegnati = clienteRepository.findByCommercialeId(currentUser.getId())
                .stream().map(Cliente::getId).toList();
        if (clienteId != null) {
            if (!clientiAssegnati.contains(clienteId)) {
                throw new ForbiddenException("Puoi vedere solo le note dei tuoi clienti"); // 403
            }
            return notaRepository.findByClienteId(clienteId);
        }
        return notaRepository.findByClienteIdIn(clientiAssegnati);
    }

    public Nota patch(UUID notaId, PatchNotaDTO dto, Utente currentUser) {
        Nota nota = getById(notaId, currentUser);
        if (dto.contenuto() != null) {
            nota.setContenuto(dto.contenuto());
        }
        nota.setDataModifica(LocalDateTime.now());
        return notaRepository.save(nota);
    }

}
