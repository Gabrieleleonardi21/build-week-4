package com.example.buildweek4.runners;

import com.example.buildweek4.entities.Cliente;
import com.example.buildweek4.entities.Fattura;
import com.example.buildweek4.entities.StatoFattura;
import com.example.buildweek4.entities.TipoSocietario;
import com.example.buildweek4.repositories.ClienteRepository;
import com.example.buildweek4.repositories.FatturaRepository;
import com.example.buildweek4.repositories.StatoFatturaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Popola clienti e fatture di esempio al primo avvio, cosi' l'API ha subito
// dati su cui lavorare senza crearli a mano da Postman. A differenza di
// DemoDataRunner resta a database: nessuna pulizia alla chiusura dell'app.
// Nessun @Profile: parte sempre, senza dover configurare nulla.
@Component
public class ClientiFattureSeeder implements CommandLineRunner {

    private static final int NUMERO_CLIENTI = 5;
    private static final int FATTURE_PER_CLIENTE = 2;

    private final ClienteRepository clienteRepository;
    private final FatturaRepository fatturaRepository;
    private final StatoFatturaRepository statoFatturaRepository;

    public ClientiFattureSeeder(ClienteRepository clienteRepository, FatturaRepository fatturaRepository,
                                StatoFatturaRepository statoFatturaRepository) {
        this.clienteRepository = clienteRepository;
        this.fatturaRepository = fatturaRepository;
        this.statoFatturaRepository = statoFatturaRepository;
    }

    @Override
    public void run(String... args) {
        // guardia semplice sull'intera tabella, non sul singolo cliente come in
        // DataSeeder: qui il compito e' riempire una tabella vuota, non
        // garantire l'esistenza di record specifici
        if (clienteRepository.count() > 0) {
            return;
        }

        StatoFattura bozza = statoFatturaRepository.findByNome("BOZZA")
                .orElseGet(() -> statoFatturaRepository.save(new StatoFattura("BOZZA")));

        TipoSocietario[] tipi = TipoSocietario.values();
        List<Cliente> clienti = new ArrayList<>();
        for (int i = 1; i <= NUMERO_CLIENTI; i++) {
            Cliente cliente = new Cliente(
                    "Cliente Demo " + i,
                    "IT" + String.format("%09d", i),
                    "cliente" + i + "@demo.epicenergy.it",
                    BigDecimal.valueOf(10_000L * i),
                    tipi[(i - 1) % tipi.length],
                    null,
                    null,
                    null
            );
            clienti.add(clienteRepository.save(cliente));
        }

        int numeroProgressivo = 1;
        for (Cliente cliente : clienti) {
            for (int j = 0; j < FATTURE_PER_CLIENTE; j++) {
                Fattura fattura = new Fattura(
                        LocalDate.now().minusDays(numeroProgressivo),
                        BigDecimal.valueOf(100L * numeroProgressivo),
                        "DEMO-" + String.format("%04d", numeroProgressivo),
                        cliente,
                        bozza
                );
                fatturaRepository.save(fattura);
                numeroProgressivo++;
            }
        }

        System.out.println("ClientiFattureSeeder: creati " + clienti.size() + " clienti e "
                + (clienti.size() * FATTURE_PER_CLIENTE) + " fatture.");
    }
}
