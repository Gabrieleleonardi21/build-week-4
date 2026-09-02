package com.example.buildweek4.repositories;

import com.example.buildweek4.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {
    // usata da UtenteService.loadUserByUsername (login) e dal controllo email duplicata in registrazione
    Optional<Utente> findByEmail(String email);
}
