package com.example.buildweek4.controllers;

import com.example.buildweek4.services.NotaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*

 **************** NOTE CRUD ****************
- POST http://localhost:5432/api/note —> {payload nota} → commercialeId ricavato dal token
- GET http://localhost:5432/api/note lista note
- GET http://localhost:5432/api/note/{notaId}
- PATCH http://localhost:5432/api/note/{notaId} —> {payload modifica nota, solo se propria o cliente assegnato da Admin}

*/

@RestController
@RequestMapping("/api/note")
public class NoteController {

    private final NotaService notaService;
}
