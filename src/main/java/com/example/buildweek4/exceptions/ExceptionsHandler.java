package com.example.buildweek4.exceptions;

import com.example.buildweek4.dto.ErrorsDTO;
import com.example.buildweek4.entities.Utente;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        // il nome del campo va incluso: con piu' campi mancanti il solo
        // getDefaultMessage() ripete lo stesso testo N volte ("non deve essere
        // spazio, non deve essere spazio, ...") e chi chiama non sa cosa correggere
        String messaggi = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new ErrorsDTO(messaggi, LocalDateTime.now());
    }

    // Un @PreAuthorize non superato fa lanciare a Spring Security una
    // AuthorizationDeniedException, sottoclasse di AccessDeniedException.
    // Senza questo handler l'eccezione risale fino all'ExceptionTranslationFilter,
    // che risponde 403 con il corpo VUOTO: il client vede solo lo status e non
    // sa se il problema e' il ruolo, il token o l'endpoint sbagliato.
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorsDTO handleAccessDenied() {
        // ex.getMessage() vale sempre "Access Denied", quindi non lo usiamo:
        // piu' utile dire con quale ruolo e' arrivata la richiesta, cosi' chi
        // chiama capisce subito che deve autenticarsi con un altro utente
        return new ErrorsDTO("Non hai i permessi necessari per questa operazione. Ruolo attuale: "
                + ruoloUtenteCorrente(), LocalDateTime.now());
    }

    // 403 lanciato a mano dai service (es. solo l'ADMIN puo' portare una fattura
    // in INSOLUTA): qui il messaggio e' gia' scritto da noi e va restituito com'e'
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorsDTO handleForbidden(ForbiddenException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    // 401: token mancante, scaduto o non valido. Arriva dal JwtFilter, che non
    // potendo usare un @ExceptionHandler direttamente (e' fuori dal controller)
    // gira l'eccezione all'HandlerExceptionResolver, il quale finisce qui
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    public ErrorsDTO handleUnauthorized(UnauthorizedException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    // Le tre eccezioni qui sotto hanno gia' un @ResponseStatus, quindi lo status
    // code era corretto anche senza handler: quello che mancava era il CORPO nel
    // nostro formato. Senza, Spring rispondeva col body di default di /error
    // ({timestamp, status, error, message, path}) e il client avrebbe dovuto
    // leggere il messaggio in due modi diversi a seconda dello status.
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorsDTO handleNotFound(NotFoundException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsDTO handleBadRequest(BadRequestException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(EntityInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public ErrorsDTO handleEntityInUse(EntityInUseException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    // il principal e' l'Utente messo nel context dal JwtFilter; resta null solo
    // sugli endpoint pubblici, dove nessuna autenticazione e' stata fatta
    private String ruoloUtenteCorrente() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Utente utente) {
            return utente.getRuolo().name();
        }
        return "nessuno (utente non autenticato)";
    }
}
