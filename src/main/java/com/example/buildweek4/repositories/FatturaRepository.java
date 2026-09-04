package com.example.buildweek4.repositories;

import com.example.buildweek4.entities.Fattura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, UUID> {
    // i filtri sono opzionali: se un parametro arriva null la relativa condizione
    // viene ignorata, cosi' la stessa query copre tutte le combinazioni
    @Query("SELECT f FROM Fattura f WHERE " + "(:clienteId IS NULL OR f.cliente.id = :clienteId) AND " + "(:statoId IS NULL OR f.stato.id = :statoId)")
    Page<Fattura> filtra(@Param("clienteId") UUID clienteId, @Param("statoId") UUID statoId, Pageable pageable);

    boolean existsByStatoId(UUID statoId);

    // usata dal vincolo di cancellazione del cliente
    boolean existsByClienteId(UUID clienteId);
}
