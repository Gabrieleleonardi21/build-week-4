package com.example.buildweek4.controllers;

import com.example.buildweek4.dto.NotaResponseDTO;
import com.example.buildweek4.dto.NuovaNotaDTO;
import com.example.buildweek4.dto.PatchNotaDTO;
import com.example.buildweek4.entities.Utente;
import com.example.buildweek4.services.NotaService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/*

 **************** NOTE CRUD ****************
- POST http://localhost:5432/api/note —> {payload nota} → commercialeId ricavato dal token
- GET http://localhost:5432/api/note
- GET http://localhost:5432/api/note/{notaId}
- PATCH http://localhost:5432/api/note/{notaId} —> {payload modifica nota, solo se propria o cliente assegnato da Admin}

*/

@RestController
@RequestMapping("/api/note")
public class NoteController {
    private final NotaService notaService;

    public NoteController(NotaService notaService) {
        this.notaService = notaService;
    }

    // 1. POST http://localhost:5432/api/note —> {payload nota} → commercialeId ricavato dal token
    @PreAuthorize("hasAnyRole('COMMERCIALE', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping
    public NotaResponseDTO creaNota(@Validated @RequestBody NuovaNotaDTO dto, @AuthenticationPrincipal Utente currentUser) {
        return NotaResponseDTO.from(this.notaService.save(dto, currentUser));
    }

    // 2. GET http://localhost:5432/api/note
    @PreAuthorize("hasAnyRole('COMMERCIALE', 'ADMIN')")
    @GetMapping
    public List<NotaResponseDTO> getNote(@RequestParam(required = false) UUID clienteId, @AuthenticationPrincipal Utente currentUser) {
        return this.notaService.getNote(clienteId, currentUser).stream()
                .map(NotaResponseDTO::from)
                .toList();
    }

    // 3. GET http://localhost:5432/api/note/{notaId}
    @PreAuthorize("hasAnyRole('COMMERCIALE', 'ADMIN')")
    @GetMapping("/{notaId}")
    public NotaResponseDTO getNotaById(@PathVariable UUID notaId, @AuthenticationPrincipal Utente currentUser) {
        return NotaResponseDTO.from(this.notaService.getById(notaId, currentUser));
    }

    // 4. PATCH http://localhost:5432/api/note/{notaId} —> {payload modifica nota, solo se propria o cliente assegnato da Admin}
    @PreAuthorize("hasAnyRole('COMMERCIALE', 'ADMIN')")
    @PatchMapping("/{notaId}")
    public NotaResponseDTO patchNota(@PathVariable UUID notaId, @RequestBody PatchNotaDTO dto, @AuthenticationPrincipal Utente currentUser) {
        return NotaResponseDTO.from(this.notaService.patch(notaId, dto, currentUser));
    }

}
