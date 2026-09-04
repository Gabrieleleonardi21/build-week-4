package com.example.buildweek4.repositories;

import com.example.buildweek4.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    // partitaIva ed email sono unique a database: questi controlli servono a
    // restituire un 400 leggibile invece del 500 del vincolo violato
    boolean existsByPartitaIva(String partitaIva);

    boolean existsByEmail(String email);

    // clienti assegnati a un commerciale: usato per filtrare le note visibili
    List<Cliente> findByCommercialeId(UUID commercialeId);
}
