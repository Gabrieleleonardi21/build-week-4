package com.example.buildweek4.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Indirizzi")
@ToString
@Getter
@Setter

public class Indirizzi {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(length = 255, nullable = false)
    private String via;
    @Column(length = 20, nullable = false)
    private String civico;
    @Column(length = 100, nullable = false)
    private String citta;
    @Column(length = 50, nullable = false)
    private String provincia;
    @Column(length = 10, nullable = false)
    private String cap;
    @Column(nullable = false, updatable = false)
    private LocalDateTime data_creazione;
    @Column(nullable = false)
    private LocalDateTime data_ultima_modifica;


    public Indirizzi(){}

    @OneToMany
    @JoinColumn(name = "sede_legale_id", nullable = false)
    private Clienti sede_legale;
    @OneToMany
    @JoinColumn(name = "sede_operativa_id", nullable = false)
    private Clienti sede_operativa;

    public Indirizzi(String via, String civico, String citta, String provincia, String cap, Clienti sede_legale, Clienti sede_operativa) {
        this.via = via;
        this.civico = civico;
        this.citta = citta;
        this.provincia = provincia;
        this.cap = cap;
    }
}

