package com.example.buildweek4.repositories;

import com.example.buildweek4.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FatturaRepository extends JpaRepository<Fattura, UUID> {
}
