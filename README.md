**English** | [Español](README.es.md)

# GestBanda API

[![CI](https://github.com/ManuelJesusPascual/B-GestBanda/actions/workflows/ci.yml/badge.svg)](https://github.com/ManuelJesusPascual/B-GestBanda/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

REST API for managing marching bands (Spanish *bandas cofrades*): members, their roles, the voices (instrument parts) a band plays and the invitation codes used to join it.

It is the backend of [F-GestBanda](https://github.com/ManuelJesusPascual/F-GestBanda), a Flutter app.

## Why

Most bands keep their member list, who plays which part and who has been invited in spreadsheets and group chats. GestBanda keeps all of that in one place. The band director creates the band, generates time-limited invitation codes for a given voice and manages the members. Musicians join with a code.

## Features

- Sign-up and login with JWT (stateless, BCrypt-hashed passwords).
- Create a band with a logo (uploaded to Imgur). The creator becomes its `DIRECTOR`.
- Invitation codes per band and voice, with an expiry date: create, list, update and delete.
- Join a band with a code.
- Manage the voices of a band: add, list, list the missing ones and remove. Removing a voice moves its members and codes to *Sin Voz*.
- Manage members: list, change their role and voice, and remove them.
- Per-band authorization based on the role of the user in the token.
- Input validation with clear error messages and consistent error responses.

## Tech stack

| | |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.2 (Web MVC, Data JPA, Security) |
| Auth | JWT (jjwt) + BCrypt |
| Database | PostgreSQL 16, schema managed with Liquibase |
| Tests | JUnit 5, Mockito, Testcontainers |
| Build and run | Maven, Docker, Docker Compose |
| CI | GitHub Actions |

## Architecture

```
controllers  ->  services  ->  repositories  ->  PostgreSQL
    |               |
   DTOs         entities (JPA)
```

- **Controllers** receive the request and validate the DTO.
- **Services** hold the business rules and transactions.
- **Repositories** are Spring Data JPA interfaces.
- **Security**: `JWTAuthenticationFilter` handles `POST /api/users/login` and returns the token in the `Authorization` header. `JWTAuthorizationFilter` checks the token on every other request.
- **Errors**: services throw `GestBandaException` with an `ErrorCodes` value, and `GlobalExceptionHandler` turns it into a JSON error with the matching HTTP status.

Documentation: [entity-relationship model](https://dbdiagram.io/d/676b8a825406798ef797c0e0) · [technical diagram](https://app.eraser.io/workspace/YplnmLy3G8KaqIGRt6Uz?origin=share)

## Getting started

Requirements: Docker.

```bash
git clone https://github.com/ManuelJesusPascual/B-GestBanda.git
cd B-GestBanda
docker compose up --build
```

The API starts on `http://localhost:3000` with a PostgreSQL database. Liquibase creates the schema and the seed data (roles and voices) on startup.

### Configuration

Settings are read from environment variables. With Docker Compose, put them in a `.env` file (see [`.env.example`](.env.example)).

| Variable | Required | Description |
|---|---|---|
| `JWT_SECRET` | Yes (Compose uses a dev-only default) | HMAC key used to sign tokens, at least 32 bytes. |
| `IMGUR_CLIENT_ID` | To register bands | Imgur Client-ID used to upload the band logo. |
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | No | Database connection. Default: `jdbc:postgresql://localhost:5432/gestbanda`, `postgres`, `postgres`. |

### Running without Docker

Requires JDK 21 and a PostgreSQL database named `gestbanda`.

```bash
export JWT_SECRET=$(openssl rand -base64 64)
./mvnw spring-boot:run
```

## API

All endpoints except sign-up and login need the header `Authorization: Bearer <token>`. Without a valid token the API returns `401`.

### Permissions

The user is always the one in the token; a `user` sent in the body is ignored. Each member has one role per band:

| Role | Can |
|---|---|
| `DIRECTOR` | Everything in the band: members, roles, voices and invitation codes. |
| `JEFE DE VOZ` (voice leader) | What a member can, plus change the voice of, or remove, the `COMPONENTE` members of their own voice. |
| `COMPONENTE` (member) | See the band, its members and voices, and leave the band. |

Anything else returns `403` (`code: 12`). The full list per endpoint is in [API_CHANGES.md](API_CHANGES.md).

| Method | Path | Description |
|---|---|---|
| POST | `/api/users/registerUser` | Sign up. Returns the user and the token. |
| POST | `/api/users/login` | Log in. Returns the user and the token. |
| GET | `/api/users/getUserByID?id=` | Get a user. |
| POST | `/api/band/registerBand` | Create a band (multipart: `createBandRequest` JSON + `img`). |
| GET | `/api/userBands/getUserBands?id=` | Bands of a user. |
| GET | `/api/userBands/getBandUsers?id=` | Members of a band. |
| PUT | `/api/userBands/modifyUserBand` | Change a member's role and voice. |
| DELETE | `/api/userBands/deleteUserBand?idUser=&idBand=` | Remove a member from a band. |
| POST | `/api/codes/createBandCode` | Create an invitation code. |
| GET | `/api/codes/getBandCodes?id=` | Codes of a band. |
| PUT | `/api/codes/updateBandCode` | Change a code's voice and expiry date. |
| DELETE | `/api/codes/deleteBandCode?idCode=` | Delete a code. |
| POST | `/api/codes/joinBandByCode` | Join a band with a code. |
| POST | `/api/bandVoices/addVoiceToBand` | Add a voice to a band. |
| GET | `/api/bandVoices/getVoicesBand?id=` | Voices of a band. |
| GET | `/api/bandVoices/getVoicesBandNotHave?id=` | Voices the band does not have yet. |
| DELETE | `/api/bandVoices/deleteVoiceBand?idVoice=&idBand=` | Remove a voice from a band. |
| GET | `/api/roles/getAllRoles` | List the roles. |

### Examples

Sign up and keep the token:

```bash
curl -i -X POST http://localhost:3000/api/users/registerUser \
  -H "Content-Type: application/json" \
  -d '{"name":"Ana","email":"ana@example.com","phone":"600000001","password":"Password1!"}'
```

```http
HTTP/1.1 201
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9...

{"id":"fa4942be-a6c8-4f55-a963-426f7aa68b27","name":"Ana","email":"ana@example.com","phone":"600000001"}
```

Log in:

```bash
curl -i -X POST http://localhost:3000/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ana@example.com","password":"Password1!"}'
```

Create a band (requires `IMGUR_CLIENT_ID`):

```bash
curl -X POST http://localhost:3000/api/band/registerBand \
  -H "Authorization: Bearer $TOKEN" \
  -F 'createBandRequest={"name":"Banda Ejemplo"};type=application/json' \
  -F 'img=@logo.png'
```

Create an invitation code and join with it:

```bash
curl -X POST http://localhost:3000/api/codes/createBandCode \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"band_id":"<band id>","voice_id":"<voice id>","end_date":"2026-12-31T23:59:59Z"}'

curl -X POST http://localhost:3000/api/codes/joinBandByCode \
  -H "Authorization: Bearer $OTHER_TOKEN" -H "Content-Type: application/json" \
  -d '{"code":"482913"}'
```

Errors always have the same shape:

```json
{ "timeMark": "2026-11-27T19:40:11.714+00:00", "message": "Ese codigo ha expirado. Por favor contacta con el director de la banda", "code": 9 }
```

## Tests

```bash
./mvnw verify
```

Unit tests cover sign-up and login validation, band registration and joining by code. Integration tests run the API against PostgreSQL with Testcontainers and check the permissions of each role, the `401`/`403`/`404` responses and the role given when joining. Docker must be running.

## Known limitations

- **Long-lived tokens, no refresh or revocation.** A JWT is valid for 30 days and cannot be revoked before it expires. A production version would use short-lived access tokens with refresh tokens.
- **Manual input validation.** Each request DTO validates itself in a `validate()` method instead of using Bean Validation (`@Valid`, `@NotNull`...). It works and returns clear messages, but it is more code than the standard approach.
- **Image hosting depends on Imgur.** Registering a band fails (`424`) without `IMGUR_CLIENT_ID`.

## Roadmap

- Instruments: band inventory and instruments assigned to each member.
- Uniforms: band uniforms and sizes per member.
- Events and trips: performances, meeting point and time, and who attends.

## Authors

- [@ManuelJesusPascual](https://github.com/ManuelJesusPascual): lead developer.
- [@DevJoseEstrada](https://github.com/DevJoseEstrada): project architecture, support and best practices.

## License

[MIT](LICENSE) © Manuel Jesús Pascual
