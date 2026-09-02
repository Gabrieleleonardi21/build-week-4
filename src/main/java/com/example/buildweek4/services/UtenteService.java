package com.example.buildweek4.services;

import com.example.buildweek4.dto.RegisterRequestDTO;
import com.example.buildweek4.entities.Ruolo;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.exceptions.BadRequestException;
import com.example.buildweek4.repositories.UtenteRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtenteService implements UserDetailsService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));
    }

    public Utente register(RegisterRequestDTO dto) {
        if (utenteRepository.findByEmail(dto.email()).isPresent()) {
            throw new BadRequestException("Email " + dto.email() + " già in uso");
        }
        Utente utente = new Utente(
                dto.email(),
                passwordEncoder.encode(dto.password()),
                dto.nome(),
                dto.cognome(),
                Ruolo.USER
        );
        return utenteRepository.save(utente);
    }
}
