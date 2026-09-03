package com.example.buildweek4.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 403 e non 401: l'utente e' autenticato correttamente, semplicemente
// il suo ruolo non basta per questa specifica operazione
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
