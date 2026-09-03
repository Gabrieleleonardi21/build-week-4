package com.example.buildweek4.runners;

import com.example.buildweek4.entities.Ruolo;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Crea gli utenti iniziali al primo avvio: senza un ADMIN a database nessuno
// potrebbe promuovere nessuno, perche' la registrazione assegna sempre USER.
@Component
public class DataSeeder implements CommandLineRunner {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${seed.admin.email}")
    private String adminEmail;
    @Value("${seed.admin.password}")
    private String adminPassword;
    @Value("${seed.commerciale.email}")
    private String commercialeEmail;
    @Value("${seed.commerciale.password}")
    private String commercialePassword;
    @Value("${seed.user.email}")
    private String userEmail;
    @Value("${seed.user.password}")
    private String userPassword;

    public DataSeeder(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        creaSeMancante(adminEmail, adminPassword, "Admin", "Iniziale", Ruolo.ADMIN);
        creaSeMancante(commercialeEmail, commercialePassword, "Commerciale", "Iniziale", Ruolo.COMMERCIALE);
        creaSeMancante(userEmail, userPassword, "User", "Iniziale", Ruolo.USER);
    }

    // idempotente: a ogni riavvio ricontrolla e non duplica nulla.
    // Una password cambiata in env.properties non viene riapplicata a un utente
    // gia' esistente: in quel caso va aggiornata dall'endpoint o a database.
    private void creaSeMancante(String email, String password, String nome, String cognome, Ruolo ruolo) {
        if (utenteRepository.findByEmail(email).isPresent()) {
            return;
        }
        Utente utente = new Utente(email, passwordEncoder.encode(password), nome, cognome, ruolo);
        utenteRepository.save(utente);
        System.out.println("DataSeeder: creato utente " + ruolo + " con email " + email);
    }
}
