package com.example.buildweek4.repositories;

import com.example.buildweek4.entities.Indirizzo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IndirizzoRepository extends JpaRepository<Indirizzo, UUID> {
}
