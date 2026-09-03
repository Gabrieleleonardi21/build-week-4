package com.example.buildweek4.controllers;

import com.example.buildweek4.dto.NuovoIndirizzoDTO;
import com.example.buildweek4.dto.PatchIndirizzoDTO;
import com.example.buildweek4.entities.Indirizzo;
import com.example.buildweek4.services.IndirizzoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/*

  **************** INDIRIZZI CRUD ****************
- POST /api/indirizzi —> {payload nuovo indirizzo} ritorna CREATED
- GET /api/indirizzi
- GET /api/indirizzi/{indirizzoId}
- PUT http://localhost:5432/api/indirizzi/{indirizzoId} —> {payload modifica completa indirizzo}
- PATCH http://localhost:5432/api/indirizzi/{indirizzoId} —> {payload modifica campo singolo indirizzo}

 */

@RestController
@RequestMapping("/api/indirizzi")
public class IndirizziController {
    private final IndirizzoService indirizzoService;

    public IndirizziController(IndirizzoService indirizzoService) {
        this.indirizzoService = indirizzoService;
    }

    //1. POST /api/indirizzi —> {payload nuovo indirizzo} ritorna CREATED
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('COMMERCIALE', 'ADMIN')")
    public Indirizzo creaIndirizzo(@Validated @RequestBody NuovoIndirizzoDTO dto) {
        return this.indirizzoService.save(dto);
    }

    //2. GET /api/indirizzi
    @GetMapping
    public List<Indirizzo> getIndirizzi() {
        return this.indirizzoService.getAll();
    }

    //3. GET /api/indirizzi/{indirizzoId}
    @GetMapping("/{indirizzoId}")
    public Indirizzo getIndirizzoById(@PathVariable UUID indirizzoId) {
        return this.indirizzoService.getById(indirizzoId);
    }

    //4. PUT http://localhost:5432/api/indirizzi/{indirizzoId} —> {payload modifica completa indirizzo}
    @PutMapping("/{indirizzoId}")
    @PreAuthorize("hasAnyRole('COMMERCIALE', 'ADMIN')")
    public Indirizzo modificaIndirizzo(@PathVariable UUID indirizzoId,@Validated @RequestBody NuovoIndirizzoDTO dto) {
        return this.indirizzoService.modificaIndirizzo(indirizzoId, dto);
    }

    //5. PATCH http://localhost:5432/api/indirizzi/{indirizzoId} —> {payload modifica campo singolo indirizzo}
    @PatchMapping("/{indirizzoId}")
    @PreAuthorize("hasAnyRole('COMMERCIALE', 'ADMIN')")
    public Indirizzo patchIndirizzo(@PathVariable UUID indirizzoId,@Validated @RequestBody PatchIndirizzoDTO dto) {
        return this.indirizzoService.patchIndirizzo(indirizzoId, dto);
    }
}
