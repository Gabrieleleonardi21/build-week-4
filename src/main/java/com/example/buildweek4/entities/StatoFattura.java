package com.example.buildweek4.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stati_fattura")
@Getter
@Setter
@ToString
public class StatoFattura {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(unique = true)
    private String nome;

    // da valorizzare nel service: dataCreazione al salvataggio, dataModifica a ogni update
    @Column(name = "creato_il", updatable = false)
    private LocalDateTime dataCreazione;

    @Column(name = "modificato_il")
    private LocalDateTime dataModifica;

    @PrePersist
    private void onCreazione() {
        this.dataCreazione = LocalDateTime.now();
        this.dataModifica = LocalDateTime.now();
    }

    @PreUpdate
    private void onModifica() {
        this.dataModifica = LocalDateTime.now();
    }

    public StatoFattura(){}

    public StatoFattura(String nome){this.nome = nome;}
}
