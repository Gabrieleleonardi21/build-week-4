package com.example.buildweek4.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clienti")
@Getter
@Setter
@ToString
public class Clienti {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private String ragioneSociale;

   @Column(nullable = false, unique = true)
   private String partitaIva;

   @Column(nullable = false, unique = true)
   private String email;

   private BigDecimal fatturatoAnnuale;

   @Enumerated(EnumType.STRING)
    private TipoSocietario tipo;

   private String logoAziendale;

   @ManyToOne
    @JoinColumn(name = "commerciale_id")
    @ToString.Exclude
    private Utente commerciale;

   @ManyToOne
    @JoinColumn(name = "sede_legale_id")
    @ToString.Exclude
    private Indirizzo sedeLegale;

   @ManyToOne
    @JoinColumn(name = "sede_operativa_id")
    @ToString.Exclude
    private  Indirizzo sedeOperativa;

   @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private LocalDateTime dataCreazione;

   @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private LocalDateTime dataUltimaModifica;

   public Clienti(){}

    public Clienti(String ragioneSociale, String partitaIva, String email, BigDecimal fatturatoAnnuale,
                   TipoSocietario tipo, String logoAziendale, Indirizzo sedeLegale, Indirizzo sedeOperativa){
       this.ragioneSociale = ragioneSociale;
       this.partitaIva = partitaIva;
       this.email = email;
       this.fatturatoAnnuale = fatturatoAnnuale;
       this.tipo = tipo;
       this.logoAziendale = logoAziendale;
       this.sedeLegale = sedeLegale;
       this.sedeOperativa = sedeOperativa;
    }


}
