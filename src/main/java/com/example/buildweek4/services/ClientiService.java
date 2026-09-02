package com.example.buildweek4.services;

import com.example.buildweek4.entities.Cliente;
import com.example.buildweek4.payload.ModificaClienteDTO;
import com.example.buildweek4.repositories.ClientiRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ClientiService {

    private final ClientiRepository clientiRepository;

    public ClientiService(ClientiRepository clientiRepository) {
        this.clientiRepository = clientiRepository;
    }

    public Cliente modificaCliente(UUID clienteCorrenteId, ModificaClienteDTO payload) {
        Cliente cliente = this.clientiRepository.findById(clienteCorrenteId).orElseThrow(() -> new NotFoundException("Cliente con id " + clienteCorrenteId + " non trovato"));

        cliente.setRagioneSociale(payload.ragioneSociale());
        cliente.setPartitaIva(payload.partitaIva());
        cliente.setEmail(payload.email());
        cliente.setFatturatoAnnuale(payload.fatturatoAnnuale());
        cliente.setTipo(payload.tipo());
        cliente.setLogoAziendale(payload.logoAziendale());
        cliente.setSedeLegale(payload.sedeLegale());
        cliente.setSedeOperativa(payload.sedeOperativa());
        cliente.setDataModifica(LocalDateTime.now());

        return this.clientiRepository.save(cliente);
    }
}
