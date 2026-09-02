package com.example.buildweek4.repositories;

import com.example.buildweek4.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.buildweek4.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, UUID> {
    @Query("SELECT f FROM Fattura f WHERE " +
            "(:clienteId IS NULL OR f.cliente.id = :clienteId) AND " +
            "(:statoId IS NULL OR f.stato.id = :statoId)")
    List<Fattura> filtra(@Param("clienteId") UUID clienteId, @Param("statoId") UUID statoId);

    boolean existsByStatoId(UUID statoId);
}
}
