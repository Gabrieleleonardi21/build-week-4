package com.example.buildweek4.exceptions;

import com.example.buildweek4.dto.ErrorsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionsHandler {

    // Quando il payload in ingresso non rispetta le regole di validazione (@NotBlank, @NotNull...)
    // Spring blocca la richiesta PRIMA che arrivi al controller e lancia questa eccezione.
    // Non basta gestire ValidationException: quella è l'eccezione generica di Bean Validation,
    // mentre questa (MethodArgumentNotValidException) è quella specifica che Spring MVC lancia
    // per un @RequestBody/@Validated non valido, e non è una sua sottoclasse quindi un handler
    // su ValidationException non la intercetterebbe mai.
    // Senza questo handler, il client riceverebbe una risposta 400 con un corpo generico e poco leggibile,
    // perdendo i messaggi personalizzati scritti nel DTO: qui li estraiamo e li restituiamo in modo pulito,
    // se necessario, facendo comparire a schermo una lista di errori di validazione.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsDTO handleValidationEx(MethodArgumentNotValidException ex) {
        String messaggi = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ErrorsDTO(messaggi, LocalDateTime.now());
    }

}
