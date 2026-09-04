package com.example.buildweek4.services;

import com.example.buildweek4.dto.NuovoClienteDTO;
import com.example.buildweek4.dto.PatchClienteDTO;
import com.example.buildweek4.entities.*;
import com.example.buildweek4.exceptions.BadRequestException;
import com.example.buildweek4.exceptions.EntityInUseException;
import com.example.buildweek4.exceptions.NotFoundException;
import com.example.buildweek4.dto.ModificaClienteDTO;
import com.example.buildweek4.repositories.ClienteRepository;
import com.example.buildweek4.repositories.FatturaRepository;
import com.example.buildweek4.repositories.IndirizzoRepository;
import com.example.buildweek4.repositories.NotaRepository;
import com.example.buildweek4.repositories.UtenteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final IndirizzoRepository indirizzoRepository;
    private final UtenteRepository utenteRepository;
    private final FatturaRepository fatturaRepository;
    private final NotaRepository notaRepository;

    public ClienteService(ClienteRepository clienteRepository, IndirizzoRepository indirizzoRepository,
                          UtenteRepository utenteRepository, FatturaRepository fatturaRepository,
                          NotaRepository notaRepository) {
        this.clienteRepository = clienteRepository;
        this.indirizzoRepository = indirizzoRepository;
        this.utenteRepository = utenteRepository;
        this.fatturaRepository = fatturaRepository;
        this.notaRepository = notaRepository;
    }
    public Cliente findById(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente non trovato: " + id));
    }

    public Page<Cliente> getAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public Cliente getById(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con id " + id + " non trovato"));
    }

    public Cliente save(NuovoClienteDTO dto, Utente currentUser) {
        if (clienteRepository.existsByPartitaIva(dto.partitaIva())) {
            throw new BadRequestException("Partita IVA " + dto.partitaIva() + " gia' in uso");
        }
        if (clienteRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Email " + dto.email() + " gia' in uso");
        }
        // le sedi sono facoltative: l'id viene risolto solo se presente nel body
        Indirizzo sedeLegale = null;
        if (dto.sedeLegaleId() != null) {
            sedeLegale = getIndirizzo(dto.sedeLegaleId());
        }
        Indirizzo sedeOperativa = null;
        if (dto.sedeOperativaId() != null) {
            sedeOperativa = getIndirizzo(dto.sedeOperativaId());
        }
        Cliente cliente = new Cliente(
                dto.ragioneSociale(),
                dto.partitaIva(),
                dto.email(),
                dto.fatturatoAnnuale(),
                dto.tipo(),
                dto.logoAziendale(),
                sedeLegale,
                sedeOperativa
        );
        if (currentUser.getRuolo() == Ruolo.COMMERCIALE) {
            cliente.setCommerciale(currentUser);
        }
        return clienteRepository.save(cliente);
    }

    public Cliente modificaCliente(UUID clienteCorrenteId, ModificaClienteDTO dto) {
        Cliente cliente = getById(clienteCorrenteId);

        cliente.setRagioneSociale(dto.ragioneSociale());
        cliente.setPartitaIva(dto.partitaIva());
        cliente.setEmail(dto.email());
        cliente.setFatturatoAnnuale(dto.fatturatoAnnuale());
        cliente.setTipo(dto.tipo());
        cliente.setLogoAziendale(dto.logoAziendale());
        // risolti dal repository: assegnare l'oggetto ricevuto nel body darebbe
        // a Hibernate un'entita' staccata dalla sessione
        cliente.setSedeLegale(getIndirizzo(dto.sedeLegaleId()));
        cliente.setSedeOperativa(getIndirizzo(dto.sedeOperativaId()));

        return clienteRepository.save(cliente);
    }

    public Cliente patchCliente(UUID clienteCorrenteId, PatchClienteDTO dto) {
        Cliente cliente = getById(clienteCorrenteId);

        // il controllo scatta solo se il valore cambia davvero: rimandare la stessa
        // partita IVA del cliente che si sta modificando non deve dare errore
        if (dto.partitaIva() != null && !dto.partitaIva().equals(cliente.getPartitaIva())
                && clienteRepository.existsByPartitaIva(dto.partitaIva())) {
            throw new BadRequestException("Partita IVA " + dto.partitaIva() + " gia' in uso");
        }
        if (dto.email() != null && !dto.email().equals(cliente.getEmail())
                && clienteRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Email " + dto.email() + " gia' in uso");
        }

        if (dto.ragioneSociale() != null) cliente.setRagioneSociale(dto.ragioneSociale());
        if (dto.partitaIva() != null) cliente.setPartitaIva(dto.partitaIva());
        if (dto.email() != null) cliente.setEmail(dto.email());
        if (dto.fatturatoAnnuale() != null) cliente.setFatturatoAnnuale(dto.fatturatoAnnuale());
        if (dto.tipo() != null) cliente.setTipo(dto.tipo());
        if (dto.logoAziendale() != null) cliente.setLogoAziendale(dto.logoAziendale());
        if (dto.sedeLegaleId() != null) cliente.setSedeLegale(getIndirizzo(dto.sedeLegaleId()));
        if (dto.sedeOperativaId() != null) cliente.setSedeOperativa(getIndirizzo(dto.sedeOperativaId()));
        return clienteRepository.save(cliente);
    }

    private Indirizzo getIndirizzo(UUID id) {
        return indirizzoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Indirizzo con id " + id + " non trovato"));
    }

    // Operazione dedicata riservata all'ADMIN
    public Cliente assegnaCommerciale(UUID clienteId, UUID commercialeId) {
        Cliente cliente = this.getById(clienteId);
        Utente commerciale = utenteRepository.findById(commercialeId)
                .orElseThrow(() -> new NotFoundException("Utente con id " + commercialeId + " non trovato"));

        if (commerciale.getRuolo() != Ruolo.COMMERCIALE) {
            throw new BadRequestException("L'utente indicato non ha ruolo COMMERCIALE");
        }

        cliente.setCommerciale(commerciale);
        return clienteRepository.save(cliente);
    }

    // Operazione riservata all'ADMIN
    public Cliente cambiaTipo(UUID clienteId, String nuovoTipo) {
        Cliente cliente = this.getById(clienteId);
        cliente.setTipo(parseTipo(nuovoTipo));
        return clienteRepository.save(cliente);
    }

    // stesso schema di UtenteService.cambiaRuolo: valueOf lancia IllegalArgumentException
    // sui valori non previsti, qui tradotta in un 400 con l'elenco di quelli ammessi
    private TipoSocietario parseTipo(String nuovoTipo) {
        try {
            return TipoSocietario.valueOf(nuovoTipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Tipo societario non valido: " + nuovoTipo
                    + ". Valori ammessi: PA, SAS, SPA, SRL");
        }
    }

    public void delete(UUID id) {
        Cliente cliente = this.findById(id);

        // non eliminabile se ha Fatture o Note collegate
        if (fatturaRepository.existsByClienteId(id)) {
            throw new EntityInUseException("Cliente non eliminabile: ha fatture collegate");
        }
        if (notaRepository.existsByClienteId(id)) {
            throw new EntityInUseException("Cliente non eliminabile: ha note collegate");
        }

        clienteRepository.delete(cliente);
    }
}
