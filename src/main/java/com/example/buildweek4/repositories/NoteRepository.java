package com.example.buildweek4.repositories;


import com.example.buildweek4.entities.Nota;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Nota, UUID> {

    // Per il vincolo di cancellazione del Cliente
    boolean existsByClienteId(UUID clienteId);

    // Per leggere le note di un cliente (con filtro per ruolo nel service).
    // Il grafo carica cliente e autore insieme alle note: senza, ogni nota
    // costerebbe due query in piu' per popolare la risposta
    @EntityGraph("Nota.conRelazioni")
    List<Nota> findByClienteId(UUID clienteId);

    // Versione per la lista clienti: le note di tutti i clienti in una query
    // sola, invece di una query per ogni cliente della pagina
    @EntityGraph("Nota.conRelazioni")
    List<Nota> findByClienteIdIn(Collection<UUID> clienteIds);
}
