# API changes

Changes to the GestBanda API that the frontend (F-GestBanda) must know about.
Endpoints, paths, request bodies, response bodies and the port (3000) stay the same unless listed here.

Error responses keep the current shape:

```json
{ "timeMark": "2026-11-27T19:40:11.714+00:00", "message": "...", "code": 4 }
```

## Already applied (no action needed)

| Change | Effect on the frontend |
|---|---|
| Sprint 3 endpoints (`/api/bandVoices/*`, `/api/codes/*` CRUD, `/api/userBands/modifyUserBand`, `/api/userBands/deleteUserBand`, `/api/roles/getAllRoles`) are now in `main` | None, same contract as the `01112024-LogicaGestionComponentes` branch |
| Invitation codes are generated with `SecureRandom` | None, same format (numeric string, up to 6 digits) |
| The database schema is created by Liquibase on startup | None |

## Proposed (pending approval)

### 1. The user always comes from the JWT

- `POST /api/band/registerBand`: `createBandRequest.user` is accepted and ignored. The band creator is the user in the token.
- `POST /api/codes/joinBandByCode`: `user` is accepted and ignored. The user who joins is the user in the token.
- Request and response bodies do not change. The frontend can keep sending `user`.

### 2. Authorization per band (new `403 Forbidden`)

New error code `FORBIDDEN` (`code: 12`, HTTP 403), returned with the usual error body.

| Endpoint | Who can call it |
|---|---|
| `GET /api/users/getUserByID` | The user themself, or a user who shares a band with them |
| `GET /api/userBands/getUserBands?id=` | Only when `id` is the user in the token |
| `GET /api/userBands/getBandUsers` | Members of the band |
| `PUT /api/userBands/modifyUserBand` | `DIRECTOR` of the band |
| `DELETE /api/userBands/deleteUserBand` | `DIRECTOR` of the band, or the user themself (leaving the band) |
| `GET /api/bandVoices/getVoicesBand` | Members of the band |
| `GET /api/bandVoices/getVoicesBandNotHave` | `DIRECTOR` of the band |
| `POST /api/bandVoices/addVoiceToBand`, `DELETE /api/bandVoices/deleteVoiceBand` | `DIRECTOR` of the band |
| `POST /api/codes/createBandCode`, `GET /api/codes/getBandCodes`, `PUT /api/codes/updateBandCode`, `DELETE /api/codes/deleteBandCode` | `DIRECTOR` of the band |
| `POST /api/band/registerBand`, `POST /api/codes/joinBandByCode`, `GET /api/roles/getAllRoles` | Any authenticated user |

### 3. Joining by code gives the `COMPONENTE` role

Today every user who joins with a code becomes `DIRECTOR`. After the change they get `COMPONENTE`.
The band creator is still `DIRECTOR`. Visible in the `role` field of `getBandUsers` and `getUserBands`.

### 4. Errors that return 500 today become 4xx

| Case | Today | After |
|---|---|---|
| `deleteUserBand` for a user who is not in the band | 500 `GENERIC_ERROR` ("Entity must not be null") | 404 `USER_NOT_FOUND` (3) |
| `addVoiceToBand` with a voice name that does not exist | 500 `GENERIC_ERROR` | 404 `VOICE_NOT_FOUND` (13, new) |
| Any endpoint with a voice id that does not exist | 404 `BAND_NOT_FOUND` (2) | 404 `VOICE_NOT_FOUND` (13) |
| `createBandCode` without `voice_id` | 500 `GENERIC_ERROR` | 400 `ARGUMENT_NOT_VALID` (4) |
| Any unexpected error | 500, `message` = internal exception text | 500, `message` = generic text ("Error interno del servidor") |

### 5. Requests without a valid token (optional)

Today: `403` with an empty body. Proposal: `401 Unauthorized` with the usual error body
(`code: 14`, `UNAUTHORIZED`). The frontend already removes the saved user on any non-200 from
`getUserByID`, so both behaviours log the user out; with a JSON body it can also show the message.
