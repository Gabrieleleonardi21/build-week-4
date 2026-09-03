package com.example.buildweek4.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*

  **************** INDIRIZZI CRUD ****************
- PUT http://localhost:5432/api/indirizzi/{indirizzoId} —> {payload modifica completa indirizzo}
- PATCH http://localhost:5432/api/indirizzi/{indirizzoId} —> {payload modifica campo singolo indirizzo}

 */

@RestController
@RequestMapping("/api/indirizzi")
public class IndirizziController {
}
