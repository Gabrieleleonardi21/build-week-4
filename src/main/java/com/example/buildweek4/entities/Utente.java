package com.example.buildweek4.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "utenti")
@Getter
@Setter
@ToString
public class Utente implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo ruolo;

    @Column(name = "creato_il", updatable = false)
    private LocalDateTime dataCreazione;

    @Column(name = "modificato_il")
    private LocalDateTime dataModifica;

    @PrePersist
    private void onCreazione() {
        this.dataCreazione = LocalDateTime.now();
        this.dataModifica = LocalDateTime.now();
    }

    public Utente() {}

    public Utente(String email, String password, String nome, String cognome, Ruolo ruolo) {
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
    }

    // unico punto in cui si costruiscono le authority dell'utente:
    // il prefisso "ROLE_" e' obbligatorio perche' hasRole('ADMIN') cerca l'authority "ROLE_ADMIN"
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.ruolo.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
