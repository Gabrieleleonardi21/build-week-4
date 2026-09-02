package com.example.buildweek4.repositories;

import com.example.buildweek4.entities.StatoFattura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StatoFatturaRepository extends JpaRepository<StatoFattura, UUID> {
}
