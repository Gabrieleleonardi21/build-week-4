package com.example.buildweek4.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 409 e non 400: la richiesta e' formalmente corretta, e' lo stato attuale
// dei dati a impedire la cancellazione
@ResponseStatus(HttpStatus.CONFLICT)
public class EntityInUseException extends RuntimeException {

    public EntityInUseException(String message) {
        super(message);
    }
}
