package com.example.buildweek4.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Fatture")
@ToString
@Getter
@Setter
public class Fattura {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    private BigDecimal importo;
    @Column(nullable = false, unique = true)
    private String numero;
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

    public Fattura(){}

    // le chiavi esterne sono gestite dalle relazioni: i campi cliente_id e stato_id
    // sono stati rimossi perche' mappavano le stesse colonne dei @JoinColumn qui sotto
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "stato_id", nullable = false)
    private StatoFattura stato;

    public Fattura(LocalDate data, BigDecimal importo, String numero, Cliente cliente, StatoFattura stato) {
        this.data = data;
        this.importo = importo;
        this.numero = numero;
        this.cliente = cliente;
        this.stato = stato;
    }

}
