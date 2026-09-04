package com.example.buildweek4.runners;

import com.example.buildweek4.entities.Ruolo;
import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.repositories.StatoFatturaRepository;
import com.example.buildweek4.repositories.UtenteRepository;
import com.example.buildweek4.services.StatoFatturaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

// Dati di sistema, senza i quali l'applicazione non e' utilizzabile:
// - gli utenti iniziali, perche' la registrazione assegna sempre USER e senza
//   un ADMIN a database nessuno potrebbe promuovere nessuno
// - gli stati fattura, perche' le transizioni cercano lo stato di destinazione
//   a database: senza, ogni cambio di stato fallirebbe con un 404
// @Order(1): deve girare prima di ClientiFattureSeeder, che sulle fatture demo
// si aspetta di trovare lo stato BOZZA gia' presente.
@Component
@Order(1)
public class DataSeeder implements CommandLineRunner {

    // la lista sta in StatoFatturaService e non qui: e' la stessa che protegge
    // quei nomi da rinomina e cancellazione, tenerne una copia significherebbe
    // poterle far sfasare
    private static final List<String> STATI_FATTURA = StatoFatturaService.STATI_DI_SISTEMA;

    private final UtenteRepository utenteRepository;
    private final StatoFatturaRepository statoFatturaRepository;
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

    public DataSeeder(UtenteRepository utenteRepository, StatoFatturaRepository statoFatturaRepository,
                      PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.statoFatturaRepository = statoFatturaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        creaStatiMancanti();
        creaSeMancante(adminEmail, adminPassword, "Admin", "Iniziale", Ruolo.ADMIN);
        creaSeMancante(commercialeEmail, commercialePassword, "Commerciale", "Iniziale", Ruolo.COMMERCIALE);
        creaSeMancante(userEmail, userPassword, "User", "Iniziale", Ruolo.USER);
    }

    // controllo per singolo nome e non sulla tabella intera: se qualcuno ne ha
    // cancellato uno a mano, al riavvio viene ricreato solo quello mancante
    private void creaStatiMancanti() {
        List<String> creati = STATI_FATTURA.stream()
                .filter(nome -> !statoFatturaRepository.existsByNome(nome))
                .map(nome -> statoFatturaRepository.save(new StatoFattura(nome)).getNome())
                .toList();
        if (!creati.isEmpty()) {
            System.out.println("DataSeeder: creati stati fattura " + creati);
        }
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
