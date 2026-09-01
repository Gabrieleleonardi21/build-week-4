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
public class Fatture {
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
    @Column(nullable = false)
    private UUID cliente_id;
    @Column(nullable = false)
    private UUID stato_id;
    @Column(nullable = false, updatable = false)
    private LocalDateTime data_creazione;
    @Column(nullable = false)
    private LocalDateTime data_ultima_modifica;

    public Fatture(){}

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "stato_id", nullable = false)
    private Stati_fattura stato;

    public Fatture(LocalDate data, BigDecimal importo, String numero, Cliente cliente, Stati_fattura stato) {
        this.data = data;
        this.importo = importo;
        this.numero = numero;
        this.cliente = cliente;
        this.stato = stato;
    }

}
