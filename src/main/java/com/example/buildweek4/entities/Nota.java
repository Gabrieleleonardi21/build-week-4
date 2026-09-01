package com.example.buildweek4.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "note")
@Getter
@Setter
@ToString
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String contenuto;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "autore_id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Utente autore;

    // da valorizzare nel service: dataCreazione al salvataggio, dataModifica a ogni update
    @Column(name = "creato_il", updatable = false)
    private LocalDateTime dataCreazione;

    @Column(name = "modificato_il")
    private LocalDateTime dataModifica;

    public Nota() {}

    public Nota(String contenuto, Cliente cliente, Utente autore) {
        this.contenuto = contenuto;
        this.cliente = cliente;
        this.autore = autore;
    }
}
