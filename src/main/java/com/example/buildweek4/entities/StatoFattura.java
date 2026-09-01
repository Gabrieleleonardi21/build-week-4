package com.example.buildweek4.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

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

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private LocalDateTime dataUltimaModica;

    public StatoFattura(){}

    public StatoFattura(String nome){this.nome = nome;}
}
