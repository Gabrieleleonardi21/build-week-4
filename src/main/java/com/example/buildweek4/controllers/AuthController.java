package com.example.buildweek4.controllers;

import com.example.buildweek4.dto.LoginRequestDTO;
import com.example.buildweek4.dto.LoginResponseDTO;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.security.JWTtools;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTtools jwtTools;

    public AuthController(AuthenticationManager authenticationManager, JWTtools jwtTools) {
        this.authenticationManager = authenticationManager;
        this.jwtTools = jwtTools;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );
        String token = jwtTools.generateToken((Utente) auth.getPrincipal());
        return new LoginResponseDTO(token);
    }
}
