package com.example.buildweek4.repositories;

import com.example.buildweek4.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientiRepository extends JpaRepository<Cliente, UUID> {
}
