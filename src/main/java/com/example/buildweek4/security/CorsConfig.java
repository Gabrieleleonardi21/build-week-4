package com.example.buildweek4.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// Configurazione CORS esposta come bean CorsConfigurationSource:
// in questo modo la usa direttamente Spring Security (vedi .cors() in SecurityConfig),
// che gira PRIMA di Spring MVC e altrimenti bloccherebbe il preflight OPTIONS.
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // origini esatte del frontend in sviluppo (Vite usa 5173, 5174 se la porta e' occupata).
        // In produzione va aggiunto/sostituito il dominio reale del frontend.
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "http://localhost:5174"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        // "*" copre Authorization e Content-Type inviati dal frontend
        config.setAllowedHeaders(List.of("*"));
        // serve solo se il frontend usa fetch/axios con credentials (cookie); col JWT in header non e' obbligatorio
        config.setAllowCredentials(true);
        // il browser tiene in cache il preflight per 1 ora, cosi' evita un OPTIONS a ogni chiamata
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
