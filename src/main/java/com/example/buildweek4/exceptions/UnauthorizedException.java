package com.example.buildweek4.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus fa restituire 401 anche senza un @ControllerAdvice:
// l'HandlerExceptionResolver usato dal JwtFilter legge questa annotazione
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
