# API changes

Changes to the GestBanda API that the frontend (F-GestBanda) must know about.
Endpoint paths, request bodies, response bodies and the port (3000) do not change.

Error responses keep the same shape:

```json
{ "timeMark": "2026-11-27T19:40:11.714+00:00", "message": "...", "code": 4 }
```

## New error codes

| `code` | Name | HTTP | When |
|---|---|---|---|
| 12 | `FORBIDDEN` | 403 | The user is authenticated but cannot do this in this band. |
| 13 | `VOICE_NOT_FOUND` | 404 | The voice does not exist. |
| 14 | `UNAUTHORIZED` | 401 | No token, an invalid or expired token, or the user no longer exists. |

## 1. The user always comes from the JWT

- `POST /api/band/registerBand`: `createBandRequest.user` is optional and ignored. The band creator is the user in the token.
- `POST /api/codes/joinBandByCode`: `user` is optional and ignored. The user who joins is the user in the token.

The frontend can keep sending `user`.

## 2. Authorization per band (403, code 12)

| Endpoint | Who can call it |
|---|---|
| `GET /api/users/getUserByID` | The user themself, or a user who shares a band with them |
| `GET /api/userBands/getUserBands?id=` | Only when `id` is the user in the token |
| `GET /api/userBands/getBandUsers` | Members of the band |
| `PUT /api/userBands/modifyUserBand` | `DIRECTOR`: any member, any change. `JEFE DE VOZ`: `COMPONENTE` members of their own voice, voice only (changing the role returns 403) |
| `DELETE /api/userBands/deleteUserBand` | Any member to leave the band. `DIRECTOR`: any member. `JEFE DE VOZ`: `COMPONENTE` members of their own voice |
| `GET /api/bandVoices/getVoicesBand` | Members of the band |
| `GET /api/bandVoices/getVoicesBandNotHave` | `DIRECTOR` |
| `POST /api/bandVoices/addVoiceToBand`, `DELETE /api/bandVoices/deleteVoiceBand` | `DIRECTOR` |
| `POST /api/codes/createBandCode`, `GET /api/codes/getBandCodes`, `PUT /api/codes/updateBandCode`, `DELETE /api/codes/deleteBandCode` | `DIRECTOR` of the band the code belongs to |
| `POST /api/band/registerBand`, `POST /api/codes/joinBandByCode`, `GET /api/roles/getAllRoles` | Any authenticated user |

`updateBandCode` checks the voice against the band of the code. `band_id` in the body is still required but no longer decides the band.

## 3. Joining by code gives the `COMPONENTE` role

Before, every user who joined with a code became `DIRECTOR`. Now they get `COMPONENTE`.
The band creator is still `DIRECTOR`. Visible in the `role` field of `getBandUsers` and `getUserBands`.

## 4. Client errors return 4xx instead of 500

| Case | Before | Now |
|---|---|---|
| `deleteUserBand` or `modifyUserBand` for a user who is not in the band | 500 (`deleteUserBand`) or 200 doing nothing (`modifyUserBand`) | 404 `USER_NOT_FOUND` (3) |
| `addVoiceToBand` with a voice name that does not exist | 500 `GENERIC_ERROR` | 404 `VOICE_NOT_FOUND` (13) |
| A voice id that does not exist | 404 `BAND_NOT_FOUND` (2) | 404 `VOICE_NOT_FOUND` (13) |
| `createBandCode` without `voice_id` | 500 `GENERIC_ERROR` | 400 `ARGUMENT_NOT_VALID` (4) |
| A query parameter with the wrong type, such as an invalid UUID | 500 `GENERIC_ERROR` | 400 `ARGUMENT_NOT_VALID` (4) |
| Any unexpected error | 500, `message` = internal exception text | 500, `message` = "Error interno del servidor" |

## 5. Requests without a valid token return 401

Before: `403` with an empty body. Now: `401` with the usual error body (`code: 14`).

## 6. Invitation codes always have six digits

Codes are zero-padded (for example `004821`). Before, codes below 100000 had fewer digits and
`joinBandByCode` rejected them, because it requires exactly six.

## 7. Login errors use an ISO date

`POST /api/users/login` errors (wrong credentials, unreadable body) returned `timeMark` as a number
(epoch milliseconds, for example `1795810700469`). Now it is an ISO string like every other error:
`"2026-11-27T19:40:11.714+00:00"`. The rest of the body does not change.

## 8. The token is accepted with or without `Bearer `

`Authorization: Bearer <token>`, `Authorization: bearer <token>` and `Authorization: <token>` all work.
Nothing changes for clients that already send the prefix.

## No change needed

- Sprint 3 endpoints (`/api/bandVoices/*`, `/api/codes/*` CRUD, `modifyUserBand`, `deleteUserBand`, `getAllRoles`) are now in `main` with the same contract as the `01112024-LogicaGestionComponentes` branch.
- The database schema is created by Liquibase on startup.
