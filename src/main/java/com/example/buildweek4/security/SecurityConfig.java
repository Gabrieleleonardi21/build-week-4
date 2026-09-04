package com.example.buildweek4.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // attiva le annotazioni @PreAuthorize sui controller
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    // esposto come bean perche' AuthController lo inietta per autenticare email + password al login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                // abilita il CorsFilter di Spring Security, che usa il bean CorsConfigurationSource di CorsConfig:
                // gira all'inizio della catena e risponde da solo al preflight OPTIONS, senza passare dal JwtFilter
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/utenti").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        // senza questa riga, il forward interno che il container fa verso /error dopo un
                        // response.sendError(...) (es. per le nostre eccezioni @ResponseStatus) viene bloccato
                        // da anyRequest().authenticated(), e il client riceve un 403 vuoto al posto del vero
                        // status code (400/404/409/...)
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                // il JwtFilter va inserito NELLA catena di security, PRIMA del controllo di autorizzazione:
                // senza questa riga girerebbe dopo, e anyRequest().authenticated() troverebbe sempre il context vuoto → 403
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
