package com.example.buildweek4.controllers;

import com.example.buildweek4.dto.ModificaIndirizzoDTO;
import com.example.buildweek4.dto.PatchIndirizzoDTO;
import com.example.buildweek4.entities.Indirizzo;
import com.example.buildweek4.services.IndirizzoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/*

  **************** INDIRIZZI CRUD ****************
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

    //1. PUT http://localhost:5432/api/indirizzi/{indirizzoId} —> {payload modifica completa indirizzo}
    @PutMapping("/{indirizzoId}")
    @PreAuthorize("hasAnyRole('COMMERCIALE', 'ADMIN')")
    public Indirizzo modificaIndirizzo(@PathVariable UUID indirizzoId,@Validated @RequestBody ModificaIndirizzoDTO dto) {
        return this.indirizzoService.modificaIndirizzo(indirizzoId, dto);
    }

    //2. PATCH http://localhost:5432/api/indirizzi/{indirizzoId} —> {payload modifica campo singolo indirizzo}
    @PatchMapping("/{indirizzoId}")
    @PreAuthorize("hasAnyRole('COMMERCIALE', 'ADMIN')")
    public Indirizzo patchIndirizzo(@PathVariable UUID indirizzoId,@Validated @RequestBody PatchIndirizzoDTO dto) {
        return this.indirizzoService.patchIndirizzo(indirizzoId, dto);
    }
}
