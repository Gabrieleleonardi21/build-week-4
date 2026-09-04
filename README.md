# Epic Energy Services — build-week-4

API REST per la gestione di clienti e fatturazione: anagrafica clienti con sedi e note,
fatture con stati, utenti con ruoli. Spring Boot 4, PostgreSQL, autenticazione JWT.

Questo documento spiega **perché** il progetto è fatto così. Le scelte non ovvie sono
commentate anche nel codice, qui c'è il quadro d'insieme.
c'è un front-end di testing il link della repo è;
https://github.com/Gabrieleleonardi21/front-end-build-week.git
---

## Avvio

Prerequisiti: JDK 17+, PostgreSQL con un database chiamato `buildweek4`.

Il file `env.properties` **non è versionato** (contiene le password) e va creato a mano
nella cartella del progetto. Senza, l'applicazione non parte:

```properties
DB_PASSWORD=la_tua_password_postgres
JWT_SECRET=una_stringa_lunga_almeno_32_caratteri
SEED_ADMIN_PASSWORD=admin1234
SEED_COMMERCIALE_PASSWORD=commerciale1234
SEED_USER_PASSWORD=user1234
```

Poi `./mvnw spring-boot:run`. L'applicazione ascolta sulla **porta 3001**.

Al primo avvio il database viene popolato da solo (vedi *Dati iniziali*).

---

## Autenticazione

**Sessione stateless, nessun cookie.** `SessionCreationPolicy.STATELESS`: il server non
tiene nulla tra una richiesta e l'altra, l'unica cosa che identifica il chiamante è il
token nell'header `Authorization: Bearer <token>`.

**Il token contiene solo l'id dell'utente**, non il ruolo. È una scelta: il ruolo può
cambiare in qualsiasi momento (`PATCH /api/utenti/{id}/ruolo`), e se stesse dentro al
token resterebbe quello vecchio fino alla scadenza — un utente degradato continuerebbe a
comportarsi da admin per giorni. Il `JwtFilter` rilegge quindi l'utente dal database a
ogni richiesta: costa una query, ma i permessi sono sempre quelli attuali.

Conseguenza pratica: **il frontend non può leggere il ruolo dal token**. Per questo esiste
`GET /api/utenti/me`, che restituisce il profilo dell'utente autenticato.

Il token dura 7 giorni. Le password sono hashate con BCrypt (forza 12).

## Autorizzazione

I controlli stanno su **due livelli**, e la distinzione è voluta:

- **`@PreAuthorize` sul controller** quando la regola dipende solo dal ruolo.
  Esempio: `DELETE /api/fatture/{id}` è solo dell'ADMIN, si sa prima di entrare nel metodo.
- **Controllo dentro al service** quando la regola dipende dal *contenuto* della richiesta
  o dai dati a database. Esempio: il passaggio di una fattura a `INSOLUTA` è riservato
  all'ADMIN, ma l'endpoint resta aperto ai contabili — solo leggendo il body si sa quale
  stato è stato chiesto. Stessa cosa per le note, dove un commerciale vede solo quelle dei
  clienti che gli sono assegnati.

**Due trappole di Spring Security** su cui abbiamo perso tempo, per non ripeterle:

- `Utente.getAuthorities()` restituisce le authority col prefisso `ROLE_` (convenzione di
  Spring Security). Quindi si usa `hasRole('ADMIN')`, che il prefisso lo aggiunge da solo.
  `hasAuthority('ADMIN')` **compila ma nega sempre**, perché l'authority vera è `ROLE_ADMIN`.
- `hasRole()` accetta **un solo** argomento. `hasRole('CONTABILE', 'ADMIN')` compila e
  fallisce a runtime con un 500: per più ruoli si usa `hasAnyRole(...)`.

Nessuna delle due la vede il compilatore, e nemmeno `mvn compile`.

### Chi può fare cosa

| Risorsa | Lettura | Creazione | Modifica | Cancellazione |
|---|---|---|---|---|
| Clienti | autenticati | **tutti** gli autenticati | COMMERCIALE, ADMIN | ADMIN |
| Indirizzi | autenticati | COMMERCIALE, ADMIN | COMMERCIALE, ADMIN | — |
| Fatture | autenticati | CONTABILE, ADMIN | CONTABILE, ADMIN | ADMIN |
| Stati fattura | autenticati | CONTABILE, ADMIN | CONTABILE, ADMIN¹ | CONTABILE, ADMIN¹ |
| Note | COMMERCIALE², ADMIN | COMMERCIALE, ADMIN | COMMERCIALE, ADMIN | — |
| Utenti | ADMIN (lista), chiunque il proprio profilo | pubblica (registrazione) | ADMIN (ruolo) | — |

¹ non sui cinque stati di sistema, vedi sotto.
² un commerciale vede solo le note dei clienti a lui assegnati; l'admin le vede tutte.

**La creazione di un cliente è aperta a tutti gli autenticati**, USER compreso: è
voluto. Se a crearlo è un commerciale, il cliente gli viene assegnato automaticamente.

Tre operazioni sono **esclusive dell'ADMIN** perché cambiano gli equilibri del sistema e
non solo i dati: assegnare il commerciale di riferimento a un cliente, cambiare il ruolo
di un altro utente, portare una fattura in `INSOLUTA`.

---

## Fatture e stati

Gli stati sono cinque: `BOZZA`, `EMESSA`, `PAGATA`, `SCADUTA`, `INSOLUTA`.

**Ogni fattura nasce in `BOZZA`**, non è il client a deciderlo: `FatturaService.save()`
forza lo stato ignorando qualsiasi valore arrivi nel body.

**Le transizioni sono libere**: da qualsiasi stato si può passare a qualsiasi altro.
All'inizio c'era una macchina a stati rigida (`BOZZA → EMESSA → PAGATA/SCADUTA → INSOLUTA`),
ma il requisito è poter correggere una fattura in qualsiasi momento, quindi è stata
rimossa. L'unico limite è l'autorizzazione, non il percorso: `INSOLUTA` resta solo
dell'ADMIN. Uno stato inesistente dà 404.

I nomi degli stati sono **case-insensitive** in ingresso (`"emessa"` funziona), perché a
database sono salvati in maiuscolo.

### I cinque stati non si rinominano né si cancellano

`PUT` e `DELETE` su `/api/stato_fatture` rifiutano i cinque nomi di sistema con un 400,
**a prescindere dal ruolo — admin compreso**.

Non è una questione di permessi: quei nomi sono un *contratto del codice*. Il `DataSeeder`
li crea partendo da quella lista, e `FatturaService` cerca `BOZZA` per nome alla creazione
di ogni fattura. È successo davvero durante i test: una PUT ha rinominato `BOZZA`, e da
quel momento nessuno poteva più creare fatture (`NotFoundException: Stato BOZZA non
presente a database`), con le fatture esistenti finite su uno stato inventato. Un admin
che rinomina `BOZZA` rompe l'applicazione per tutti, quindi il divieto vale per tutti.

Gli stati **aggiuntivi**, creati a mano con la POST, restano invece modificabili e
cancellabili liberamente. Uno stato ancora usato da una fattura non è cancellabile.

La lista dei cinque nomi vive in un posto solo (`StatoFatturaService.STATI_DI_SISTEMA`) e
il seeder la riusa: tenerne due copie significherebbe poterle far sfasare.

---

## Gestione degli errori

**Tutte le risposte di errore hanno la stessa forma**, così il frontend legge il messaggio
sempre allo stesso modo:

```json
{ "message": "Cliente con id ... non trovato", "timestamp": "2026-09-04T12:39:45.009" }
```

Le eccezioni di dominio (`NotFoundException`, `BadRequestException`, `ForbiddenException`,
`UnauthorizedException`, `EntityInUseException`) portano il proprio status HTTP con
`@ResponseStatus`, e `ExceptionsHandler` (`@RestControllerAdvice`) costruisce il corpo.

Alcuni casi non ovvi che il handler copre:

- **403 da `@PreAuthorize`.** Un permesso negato fa lanciare a Spring Security una
  `AuthorizationDeniedException` che, senza handler, risale fino all'`ExceptionTranslationFilter`:
  il client riceve un **403 con il corpo vuoto** e non sa se il problema è il ruolo, il token
  o l'endpoint. Il messaggio dell'eccezione è sempre `"Access Denied"` e non serve a nulla,
  quindi al suo posto restituiamo il ruolo con cui la richiesta è arrivata.
- **401 dal `JwtFilter`.** Il filtro sta fuori dal `DispatcherServlet` e non può usare un
  `@ExceptionHandler`: passa l'eccezione all'`HandlerExceptionResolver`, che la fa arrivare
  comunque al nostro advice.
- **Errori di validazione.** I messaggi vengono restituiti col nome del campo davanti
  (`"email: L'email è obbligatoria, partitaIva: ..."`): senza, con più campi mancanti la
  risposta ripeteva N volte lo stesso testo senza dire cosa correggere.

In `SecurityConfig` la rotta `/error` è `permitAll`. Serve: dopo un `sendError(...)` il
container fa un forward interno verso `/error`, che `anyRequest().authenticated()`
bloccherebbe, facendo arrivare al client un 403 vuoto al posto del vero status.

---

## DTO

**In ingresso, sempre `record`.** Un DTO scritto come classe con i soli `@Getter` di Lombok
sembra funzionare ma non lo fa: senza setter né costruttore annotato Jackson non riesce a
popolarlo, i campi restano `null` e una PATCH risponde 200 senza aver modificato niente.
Coi record il costruttore canonico è sufficiente.

**In uscita, mai le entità.** I `*ResponseDTO` espongono solo i campi che servono. Le
entità hanno relazioni bidirezionali (`Indirizzo` conosce i clienti che lo usano, che a
loro volta conoscono l'indirizzo): serializzarle direttamente produce strutture ricorsive
enormi. Sui lati inversi c'è anche `@JsonIgnore` come rete di sicurezza.

**Le relazioni si passano per id, non per oggetto.** `sedeLegaleId` e non `sedeLegale`:
il service risolve l'id dal repository, così l'entità assegnata è gestita da Hibernate.
Assegnare l'oggetto arrivato nel body significherebbe passargli un'entità staccata dalla
sessione.

**PUT e PATCH non sono la stessa cosa.** La PUT sostituisce la risorsa: i campi assenti
dal body vengono azzerati. La PATCH modifica solo i campi presenti (`if (dto.campo() != null)`).
Per cambiare un solo attributo si usa la PATCH.

I controlli di unicità su partita IVA ed email vengono fatti nel service prima di salvare,
per restituire un 400 leggibile invece del 500 del vincolo violato. In PATCH il controllo
scatta solo se il valore cambia davvero, altrimenti rimandare la propria partita IVA
darebbe errore.

---

## Dati iniziali

Due `CommandLineRunner`, con `@Order` perché il secondo dipende dal primo:

- **`DataSeeder` (`@Order(1)`)** crea ciò senza cui l'applicazione non è utilizzabile: i
  cinque stati fattura e tre utenti iniziali. Serve un ADMIN a database perché la
  registrazione assegna sempre USER, quindi senza nessuno potrebbe promuovere nessuno.
  È **idempotente** e controlla record per record: se qualcuno cancella uno stato a mano,
  al riavvio viene ricreato solo quello. Le password arrivano da `env.properties`.
- **`ClientiFattureSeeder` (`@Order(2)`)** popola 5 clienti e 10 fatture di esempio, così
  l'API ha subito dati su cui lavorare. Qui la guardia è sull'intera tabella: se ci sono
  già clienti non fa nulla.

Utenti creati: `admin@buildweek4.it`, `commerciale@buildweek4.it`, `user@buildweek4.it`.

**Il CONTABILE non è tra questi** e va creato a mano: ci si registra normalmente
(nasce USER) e poi lo si promuove con `PATCH /api/utenti/{id}/ruolo` da un admin. Serve
per provare i permessi su fatture e stati: testando tutto da admin non si verifica nulla,
perché l'admin passa ovunque.

Una password cambiata in `env.properties` **non** viene riapplicata a un utente già
esistente: va aggiornata a database o dall'endpoint.

---

## Note sparse

- `spring.jpa.hibernate.ddl-auto=update`: lo schema lo genera Hibernate dalle entità.
- CORS configurato per `localhost:5173` e `5174` (Vite). In produzione va sostituito col
  dominio reale. La configurazione è esposta come bean `CorsConfigurationSource` perché la
  usi Spring Security, che gira prima di Spring MVC e altrimenti bloccherebbe il preflight.
- `open-in-view` è attivo (default di Spring Boot): la sessione Hibernate resta aperta
  durante la serializzazione. Con i `ResponseDTO` non ci si appoggia più, ma disattivarlo
  richiede di verificare gli endpoint che caricano relazioni lazy.
- Verificare le modifiche con `./mvnw test` e non solo con `compile`: query derivate,
  entity graph, espressioni `@PreAuthorize` e mapping ambigui esplodono all'avvio del
  contesto, non alla compilazione.

## Struttura

```
controllers/   endpoint REST, @PreAuthorize per i permessi di solo ruolo
services/      logica di dominio e controlli che dipendono dai dati
repositories/  Spring Data JPA
entities/      modello persistente
dto/           record in ingresso, *ResponseDTO in uscita
exceptions/    eccezioni di dominio e @RestControllerAdvice
security/      SecurityConfig, JwtFilter, JWTtools, CorsConfig
runners/       popolamento del database all'avvio
```
