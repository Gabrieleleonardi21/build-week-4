package com.example.buildweek4.controllers;

import com.example.buildweek4.dto.NuovoClienteDTO;
import com.example.buildweek4.dto.PatchClienteDTO;
import com.example.buildweek4.entities.Cliente;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clienti")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public Page<Cliente> getAll(Pageable pageable) {
        return clienteService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Cliente getById(@PathVariable UUID id) {
        return clienteService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente save(@RequestBody @Valid NuovoClienteDTO dto,
                        @AuthenticationPrincipal Utente currentUser) {
        return clienteService.save(dto, currentUser);
    }

    @PatchMapping("/{id}")
    public Cliente patch(@PathVariable UUID id, @RequestBody PatchClienteDTO dto) {
        return clienteService.patch(id, dto);
    }
}
