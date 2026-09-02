package com.example.buildweek4.controllers;

import com.example.buildweek4.entities.Cliente;
import com.example.buildweek4.payload.ModificaClienteDTO;
import com.example.buildweek4.services.ClientiService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/*

  **************** CLIENTI CRUD ****************
- PUT http://localhost:5432/api/clienti/{clienteId} —> {payload modifiche complete cliente}
- PATCH http://localhost:5432/api/clienti/{clienteId} —> {payload modifica campo singolo cliente}

 */

@RestController
@RequestMapping("/api/clienti")
public class ClientiController {

    private final ClientiService clientiService;

    public ClientiController(ClientiService clientiService) {
        this.clientiService = clientiService;
    }

    //1. PUT http://localhost:5432/api/clienti/{clienteId} —> {payload modifiche complete cliente}
    @PutMapping("/{clienteId}")
    public Cliente modificaCliente(@PathVariable UUID clienteId, @Validated @RequestBody ModificaClienteDTO payload) {
        return this.clientiService.modificaCliente(clienteId, payload);
    }

    //2. PATCH http://localhost:5432/api/clienti/{clienteId} —> {payload modifica campo singolo cliente}

}
