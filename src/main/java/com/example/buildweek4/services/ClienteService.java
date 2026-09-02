package com.example.buildweek4.services;

import com.example.buildweek4.entities.Cliente;
import com.example.buildweek4.exceptions.NotFoundException;
import com.example.buildweek4.payload.ModificaClienteDTO;
import com.example.buildweek4.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente modificaCliente(UUID clienteCorrenteId, ModificaClienteDTO payload) {
        Cliente cliente = this.clienteRepository.findById(clienteCorrenteId).orElseThrow(() -> new NotFoundException("Cliente con id " + clienteCorrenteId + " non trovato"));

        cliente.setRagioneSociale(payload.ragioneSociale());
        cliente.setPartitaIva(payload.partitaIva());
        cliente.setEmail(payload.email());
        cliente.setFatturatoAnnuale(payload.fatturatoAnnuale());
        cliente.setTipo(payload.tipo());
        cliente.setLogoAziendale(payload.logoAziendale());
        cliente.setSedeLegale(payload.sedeLegale());
        cliente.setSedeOperativa(payload.sedeOperativa());
        cliente.setDataModifica(LocalDateTime.now());

        return this.clienteRepository.save(cliente);
    }
}
