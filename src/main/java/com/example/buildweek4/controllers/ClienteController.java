package com.example.buildweek4.controllers;

import com.example.buildweek4.dto.AssegnaCommercialeDTO;
import com.example.buildweek4.dto.CambioTipoDTO;
import com.example.buildweek4.dto.ClienteResponseDTO;
import com.example.buildweek4.dto.NuovoClienteDTO;
import com.example.buildweek4.dto.PatchClienteDTO;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.dto.ModificaClienteDTO;
import com.example.buildweek4.services.ClienteService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/*

  **************** CLIENTI CRUD ****************
- PUT http://localhost:5432/api/clienti/{clienteId} —> {payload modifiche complete cliente}
- PATCH http://localhost:5432/api/clienti/{clienteId} —> {payload modifica campo singolo cliente}

 */

@RestController
@RequestMapping("/api/clienti")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    @GetMapping
    public Page<ClienteResponseDTO> getAll(Pageable pageable) {
        return clienteService.getAll(pageable).map(ClienteResponseDTO::from);
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO getById(@PathVariable UUID id) {
        return ClienteResponseDTO.from(clienteService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO save(@RequestBody @Validated NuovoClienteDTO dto,
                        @AuthenticationPrincipal Utente currentUser) {
        return ClienteResponseDTO.from(clienteService.save(dto, currentUser));
    }

    //1. PUT http://localhost:5432/api/clienti/{clienteId} —> {payload modifiche complete cliente}
    @PutMapping("/{clienteId}")
    @PreAuthorize("hasAnyRole('COMMERCIALE', 'ADMIN')")
    public ClienteResponseDTO modificaCliente(@PathVariable UUID clienteId, @Validated @RequestBody ModificaClienteDTO dto) {
        return ClienteResponseDTO.from(clienteService.modificaCliente(clienteId, dto));
    }

    //2. PATCH http://localhost:5432/api/clienti/{clienteId} —> {payload modifica campo singolo cliente}
    @PatchMapping("/{clienteId}")
    @PreAuthorize("hasAnyRole('COMMERCIALE', 'ADMIN')")
    public ClienteResponseDTO patchCliente(@PathVariable UUID clienteId, @RequestBody @Validated PatchClienteDTO dto) {
        return ClienteResponseDTO.from(clienteService.patchCliente(clienteId, dto));
    }

    // PATCH /clienti/{id}/commerciale -> solo ADMIN: assegna il commerciale che segue il cliente
    @PatchMapping("/{id}/commerciale")
    @PreAuthorize("hasRole('ADMIN')")
    public ClienteResponseDTO assegnaCommerciale(@PathVariable UUID id, @RequestBody @Validated AssegnaCommercialeDTO dto) {
        return ClienteResponseDTO.from(clienteService.assegnaCommerciale(id, dto.commercialeId()));
    }

    // PATCH /clienti/{id}/tipo -> solo ADMIN: cambia il tipo societario del cliente
    @PatchMapping("/{id}/tipo")
    @PreAuthorize("hasRole('ADMIN')")
    public ClienteResponseDTO cambiaTipo(@PathVariable UUID id, @RequestBody @Validated CambioTipoDTO dto) {
        return ClienteResponseDTO.from(clienteService.cambiaTipo(id, dto.tipo()));
    }
    // DELETE /clienti/{id} -> solo ADMIN, con i vincoli controllati nel service
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        clienteService.delete(id);
    }
}
