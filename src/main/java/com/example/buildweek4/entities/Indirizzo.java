package com.example.buildweek4.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Indirizzi")
@ToString
@Getter
@Setter

public class Indirizzo {
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
    // da valorizzare nel service: dataCreazione al salvataggio, dataModifica a ogni update
    @Column(name = "creato_il", updatable = false)
    private LocalDateTime dataCreazione;
    @Column(name = "modificato_il")
    private LocalDateTime dataModifica;


    public Indirizzo(){}

    // lato inverso delle relazioni: le colonne sede_legale_id e sede_operativa_id
    // vivono sulla tabella clienti, mappate dai campi sedeLegale e sedeOperativa di Cliente.
    // @ToString.Exclude evita di caricare l'intera lista quando si stampa l'indirizzo
    @OneToMany(mappedBy = "sedeLegale")
    @ToString.Exclude
    private List<Cliente> clientiSedeLegale;
    @OneToMany(mappedBy = "sedeOperativa")
    @ToString.Exclude
    private List<Cliente> clientiSedeOperativa;

    public Indirizzo(String via, String civico, String citta, String provincia, String cap) {
        this.via = via;
        this.civico = civico;
        this.citta = citta;
        this.provincia = provincia;
        this.cap = cap;
    }
}
