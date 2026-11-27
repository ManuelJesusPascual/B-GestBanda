[English](README.md) | **Español**

# GestBanda API

[![CI](https://github.com/ManuelJesusPascual/B-GestBanda/actions/workflows/ci.yml/badge.svg)](https://github.com/ManuelJesusPascual/B-GestBanda/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

API REST para gestionar bandas cofrades: sus componentes, los roles de cada uno, las voces que toca la banda y los códigos de invitación para unirse a ella.

Es el backend de [F-GestBanda](https://github.com/ManuelJesusPascual/F-GestBanda), una app en Flutter.

## Por qué

La mayoría de las bandas llevan la lista de componentes, quién toca cada voz y a quién han invitado en hojas de cálculo y grupos de mensajería. GestBanda lo reúne todo en un solo sitio. El director crea la banda, genera códigos de invitación con caducidad para una voz concreta y gestiona a los componentes. Los músicos se unen con un código.

## Funcionalidades

- Registro e inicio de sesión con JWT (sin estado, contraseñas cifradas con BCrypt).
- Crear una banda con su logo (se sube a Imgur). Quien la crea pasa a ser su `DIRECTOR`.
- Códigos de invitación por banda y voz, con fecha de caducidad: crear, listar, modificar y borrar.
- Unirse a una banda con un código.
- Gestionar las voces de una banda: añadir, listar, listar las que faltan y quitar. Al quitar una voz, sus componentes y códigos pasan a *Sin Voz*.
- Gestionar componentes: listar, cambiar su rol y su voz, y quitarlos de la banda.
- Permisos por banda según el rol del usuario del token.
- Validación de las entradas con mensajes claros y respuestas de error homogéneas.

## Stack

| | |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5 (Web, Data JPA, Security) |
| Autenticación | JWT (jjwt) + BCrypt |
| Base de datos | PostgreSQL 16, esquema gestionado con Liquibase |
| Tests | JUnit 5, Mockito, Testcontainers |
| Build y arranque | Maven, Docker, Docker Compose |
| CI | GitHub Actions |

## Arquitectura

```
controllers  ->  services  ->  repositories  ->  PostgreSQL
    |               |
   DTOs         entities (JPA)
```

- **Controllers**: reciben la petición y validan el DTO.
- **Services**: contienen las reglas de negocio y las transacciones.
- **Repositories**: interfaces de Spring Data JPA.
- **Seguridad**: `JWTAuthenticationFilter` atiende `POST /api/users/login` y devuelve el token en la cabecera `Authorization`. `JWTAuthorizationFilter` comprueba el token en el resto de peticiones.
- **Errores**: los services lanzan `GestBandaException` con un valor de `ErrorCodes`, y `GlobalExceptionHandler` lo convierte en un error JSON con el código HTTP correspondiente.

Documentación: [modelo entidad-relación](https://dbdiagram.io/d/676b8a825406798ef797c0e0) · [diagrama técnico](https://app.eraser.io/workspace/YplnmLy3G8KaqIGRt6Uz?origin=share)

## Puesta en marcha

Requisitos: Docker.

```bash
git clone https://github.com/ManuelJesusPascual/B-GestBanda.git
cd B-GestBanda
docker compose up --build
```

La API arranca en `http://localhost:3000` junto con una base de datos PostgreSQL. Al arrancar, Liquibase crea el esquema y los datos iniciales (roles y voces).

### Configuración

La configuración se lee de variables de entorno. Con Docker Compose, ponlas en un fichero `.env` (ver [`.env.example`](.env.example)).

| Variable | Obligatoria | Descripción |
|---|---|---|
| `JWT_SECRET` | Sí (Compose usa un valor por defecto solo para desarrollo) | Clave HMAC con la que se firman los tokens, de al menos 32 bytes. |
| `IMGUR_CLIENT_ID` | Para registrar bandas | Client-ID de Imgur con el que se sube el logo de la banda. |
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | No | Conexión a la base de datos. Por defecto: `jdbc:postgresql://localhost:5432/gestbanda`, `postgres`, `postgres`. |

### Arranque sin Docker

Necesitas JDK 21 y una base de datos PostgreSQL llamada `gestbanda`.

```bash
export JWT_SECRET=$(openssl rand -base64 64)
./mvnw spring-boot:run
```

## API

Todos los endpoints salvo el registro y el login necesitan la cabecera `Authorization: Bearer <token>`. Sin un token válido la API devuelve `401`.

### Permisos

El usuario es siempre el del token; si se envía un `user` en el body, se ignora. Cada componente tiene un rol en cada banda:

| Rol | Puede |
|---|---|
| `DIRECTOR` | Todo en la banda: componentes, roles, voces y códigos de invitación. |
| `JEFE DE VOZ` | Lo mismo que un componente y, además, cambiar de voz o quitar de la banda a los `COMPONENTE` de su voz. |
| `COMPONENTE` | Ver la banda, sus componentes y sus voces, y salir de la banda. |

Cualquier otra acción devuelve `403` (`code: 12`). La lista completa por endpoint está en [API_CHANGES.md](API_CHANGES.md).

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/users/registerUser` | Registro. Devuelve el usuario y el token. |
| POST | `/api/users/login` | Inicio de sesión. Devuelve el usuario y el token. |
| GET | `/api/users/getUserByID?id=` | Obtener un usuario. |
| POST | `/api/band/registerBand` | Crear una banda (multipart: `createBandRequest` en JSON + `img`). |
| GET | `/api/userBands/getUserBands?id=` | Bandas de un usuario. |
| GET | `/api/userBands/getBandUsers?id=` | Componentes de una banda. |
| PUT | `/api/userBands/modifyUserBand` | Cambiar el rol y la voz de un componente. |
| DELETE | `/api/userBands/deleteUserBand?idUser=&idBand=` | Quitar a un componente de la banda. |
| POST | `/api/codes/createBandCode` | Crear un código de invitación. |
| GET | `/api/codes/getBandCodes?id=` | Códigos de una banda. |
| PUT | `/api/codes/updateBandCode` | Cambiar la voz y la caducidad de un código. |
| DELETE | `/api/codes/deleteBandCode?idCode=` | Borrar un código. |
| POST | `/api/codes/joinBandByCode` | Unirse a una banda con un código. |
| POST | `/api/bandVoices/addVoiceToBand` | Añadir una voz a la banda. |
| GET | `/api/bandVoices/getVoicesBand?id=` | Voces de una banda. |
| GET | `/api/bandVoices/getVoicesBandNotHave?id=` | Voces que la banda aún no tiene. |
| DELETE | `/api/bandVoices/deleteVoiceBand?idVoice=&idBand=` | Quitar una voz de la banda. |
| GET | `/api/roles/getAllRoles` | Listar los roles. |

### Ejemplos

Registrarse y guardar el token:

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

Iniciar sesión:

```bash
curl -i -X POST http://localhost:3000/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ana@example.com","password":"Password1!"}'
```

Crear una banda (necesita `IMGUR_CLIENT_ID`):

```bash
curl -X POST http://localhost:3000/api/band/registerBand \
  -H "Authorization: Bearer $TOKEN" \
  -F 'createBandRequest={"name":"Banda Ejemplo"};type=application/json' \
  -F 'img=@logo.png'
```

Crear un código de invitación y unirse con él:

```bash
curl -X POST http://localhost:3000/api/codes/createBandCode \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"band_id":"<id de la banda>","voice_id":"<id de la voz>","end_date":"2026-12-31T23:59:59Z"}'

curl -X POST http://localhost:3000/api/codes/joinBandByCode \
  -H "Authorization: Bearer $OTHER_TOKEN" -H "Content-Type: application/json" \
  -d '{"code":"482913"}'
```

Los errores siempre tienen la misma forma:

```json
{ "timeMark": "2026-11-27T19:40:11.714+00:00", "message": "Ese codigo ha expirado. Por favor contacta con el director de la banda", "code": 9 }
```

## Tests

```bash
./mvnw verify
```

Los tests unitarios cubren la validación del registro y el login, el registro de bandas y la unión por código. Los tests de integración ejecutan la API contra PostgreSQL con Testcontainers y comprueban los permisos de cada rol, las respuestas `401`/`403`/`404` y el rol que se asigna al unirse. Docker tiene que estar en marcha.

## Limitaciones conocidas

- **Tokens de larga duración, sin refresco ni revocación.** Un JWT es válido durante 30 días y no se puede revocar antes de que caduque. Una versión para producción usaría tokens de acceso de corta duración con tokens de refresco.
- **Validación manual de las entradas.** Cada DTO de petición se valida a sí mismo con un método `validate()` en lugar de usar Bean Validation (`@Valid`, `@NotNull`...). Funciona y devuelve mensajes claros, pero supone más código que el enfoque estándar.
- **El alojamiento de imágenes depende de Imgur.** Sin `IMGUR_CLIENT_ID`, registrar una banda falla (`424`).

## Roadmap

- Instrumentos: inventario de la banda e instrumentos asignados a cada componente.
- Trajes: uniformes de la banda y tallas de cada componente.
- Eventos y viajes: actuaciones, lugar y hora de encuentro, y quién asiste.

## Autores

- [@ManuelJesusPascual](https://github.com/ManuelJesusPascual): desarrollador principal.
- [@DevJoseEstrada](https://github.com/DevJoseEstrada): arquitectura del proyecto, soporte y buenas prácticas.

## Licencia

[MIT](LICENSE) © Manuel Jesús Pascual
