package com.example.buildweek4.services;

import com.example.buildweek4.dto.NuovoClienteDTO;
import com.example.buildweek4.dto.PatchClienteDTO;
import com.example.buildweek4.entities.Cliente;
import com.example.buildweek4.entities.Indirizzo;
import com.example.buildweek4.entities.Ruolo;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.exceptions.NotFoundException;
import com.example.buildweek4.repositories.ClienteRepository;
import com.example.buildweek4.repositories.IndirizzoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final IndirizzoRepository indirizzoRepository;

    public ClienteService(ClienteRepository clienteRepository, IndirizzoRepository indirizzoRepository) {
        this.clienteRepository = clienteRepository;
        this.indirizzoRepository = indirizzoRepository;
    }

    public Page<Cliente> getAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public Cliente getById(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con id " + id + " non trovato"));
    }

    public Cliente save(NuovoClienteDTO dto, Utente currentUser) {
        Cliente cliente = new Cliente(
                dto.ragioneSociale(),
                dto.partitaIva(),
                dto.email(),
                dto.fatturatoAnnuale(),
                dto.tipo(),
                dto.logoAziendale(),
                dto.sedeLegaleId() != null ? getIndirizzo(dto.sedeLegaleId()) : null,
                dto.sedeOperativaId() != null ? getIndirizzo(dto.sedeOperativaId()) : null
        );
        if (currentUser.getRuolo() == Ruolo.COMMERCIALE) {
            cliente.setCommerciale(currentUser);
        }
        return clienteRepository.save(cliente);
    }

    public Cliente patch(UUID id, PatchClienteDTO dto) {
        Cliente cliente = getById(id);
        if (dto.getRagioneSociale() != null) cliente.setRagioneSociale(dto.getRagioneSociale());
        if (dto.getPartitaIva() != null) cliente.setPartitaIva(dto.getPartitaIva());
        if (dto.getEmail() != null) cliente.setEmail(dto.getEmail());
        if (dto.getFatturatoAnnuale() != null) cliente.setFatturatoAnnuale(dto.getFatturatoAnnuale());
        if (dto.getTipo() != null) cliente.setTipo(dto.getTipo());
        if (dto.getLogoAziendale() != null) cliente.setLogoAziendale(dto.getLogoAziendale());
        if (dto.getSedeLegaleId() != null) cliente.setSedeLegale(getIndirizzo(dto.getSedeLegaleId()));
        if (dto.getSedeOperativaId() != null) cliente.setSedeOperativa(getIndirizzo(dto.getSedeOperativaId()));
        return clienteRepository.save(cliente);
    }

    private Indirizzo getIndirizzo(UUID id) {
        return indirizzoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Indirizzo con id " + id + " non trovato"));
    }
}
