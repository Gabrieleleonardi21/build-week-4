package com.example.buildweek4.security;


import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTtools {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(Utente utente) {
        return Jwts.builder()
                .subject(utente.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getSecretKey())
                .compact();
    }

    // verifica firma e scadenza (401 se alterato/scaduto/malformato) e restituisce le claims,
    // da cui il filtro ricava l'utente
    public Claims verifyToken(String accessToken) {
        try {
            return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(accessToken).getPayload();
        } catch (JwtException e) {
            throw new UnauthorizedException("Il token fornito non è valido o è scaduto");
        }
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }



}