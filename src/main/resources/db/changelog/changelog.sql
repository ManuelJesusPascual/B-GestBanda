-- liquibase formatted sql

-- changeset mjpascual:1-initial-schema
CREATE TABLE bands
(

    id               uuid,
    name             varchar(120) NOT NULL,
    img              varchar(255),
    subscription_id  varchar(255), -- No se pone not null ya que primero se crea la banda y despues se actualiza y se inserta la suscripcion
    subscription_end timestamp,
    updated_at       timestamp,
    created_at       timestamp    NOT NULL,
    CONSTRAINT PK_BAND PRIMARY KEY (id)

);

CREATE TABLE users
(

    id         uuid,
    name       varchar(60)  NOT NULL,
    email      varchar(50)  NOT NULL UNIQUE,
    password   varchar(255) NOT NULL,
    phone      varchar(50)  NOT NULL UNIQUE,
    img        varchar(255) UNIQUE,
    updated_at timestamp,
    created_at timestamp    NOT NULL,
    CONSTRAINT PK_USERS PRIMARY KEY (id)

);


CREATE TABLE roles
(

    id         uuid,
    name       varchar(90)  NOT NULL UNIQUE,
    router     varchar(180) NOT NULL,
    updated_at timestamp,
    created_at timestamp    NOT NULL,
    CONSTRAINT PK_ROLES PRIMARY KEY (id)
);

CREATE TABLE voices
(

    id         uuid,
    name       varchar(90)  NOT NULL UNIQUE,
    updated_at timestamp,
    created_at timestamp    NOT NULL,
    CONSTRAINT PK_VOICES PRIMARY KEY (id)
);

CREATE TABLE band_voices(

                            id uuid,
                            id_band uuid NOT NULL,
                            id_voice uuid NOT NULL,
                            updated_at timestamp,
                            created_at timestamp NOT NULL,
                            CONSTRAINT PK_BAND_VOICES PRIMARY KEY (id),
                            CONSTRAINT FK_BAND_VOICES_BAND FOREIGN KEY (id_band) REFERENCES bands(id),
                            CONSTRAINT FK_BAND_VOICES_VOICE FOREIGN KEY (id_voice) REFERENCES voices(id),
                            CONSTRAINT UNIQUE_BAND_VOICES UNIQUE (id_band, id_voice)

);

CREATE INDEX I_band_voices_band ON band_voices (id_band);
CREATE INDEX I_band_voices_voices ON band_voices (id_voice);

CREATE TABLE user_band
(
    id         uuid,
    rol_id     uuid      NOT NULL,
    user_id    uuid      NOT NULL,
    band_id    uuid      NOT NULL,
    voice_id   uuid      NOT NULL,
    updated_at timestamp,
    created_at timestamp NOT NULL,
    CONSTRAINT PK_USER_BAND PRIMARY KEY (id),
    CONSTRAINT FK_USER_BAND_ROL FOREIGN KEY (rol_id) references roles (id),
    CONSTRAINT FK_USER_BAND_USER FOREIGN KEY (user_id) references users (id),
    CONSTRAINT FK_USER_BAND_VOICE FOREIGN KEY (band_id,voice_id) references band_voices(id_band,id_voice),
    CONSTRAINT UNIQUE_USER_BAND UNIQUE (user_id, band_id)

);

CREATE INDEX I_user_band_rol ON user_band (rol_id);
CREATE INDEX I_user_band_user ON user_band (user_id);
CREATE INDEX I_user_band_band ON user_band (band_id);
CREATE INDEX I_user_band_voice ON user_band (voice_id);


CREATE TABLE codes
(

    id         uuid,
    code       varchar(15) NOT NULL UNIQUE,
    end_date   timestamp   NOT NULL,
    band_id    uuid        NOT NULL,
    voice_id   uuid,
    updated_at timestamp,
    created_at timestamp   NOT NULL,
    CONSTRAINT PK_CODE PRIMARY KEY (id),
    CONSTRAINT FK_CODE_VOICE FOREIGN KEY (band_id,voice_id) REFERENCES band_voices(id_band,id_voice)
);

CREATE INDEX I_codes_band ON codes (band_id);
CREATE INDEX I_codes_voice ON codes (voice_id);


INSERT INTO roles(id, name, router, created_at)
VALUES (gen_random_uuid(), 'COMPONENTE', 'userRouter', now());
INSERT INTO roles(id, name, router, created_at)
VALUES (gen_random_uuid(), 'JEFE DE VOZ', 'voiceLeaderRouter', now());
INSERT INTO roles(id, name, router, created_at)
VALUES (gen_random_uuid(), 'DIRECTOR', 'directorRouter', now());


INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Sin Voz',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Corneta 1º Quinto',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Corneta 1º Fuerte',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Corneta 1º Piano',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Trompeta 1º Fuerte',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Trompeta 1º Piano',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Trompeta 2º Piano',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Trompeta 2º Fuerte',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Tuba 1º',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Tuba 2º',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Trompa 1º',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Trompa 2º',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Tambor',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Caja',now());
INSERT INTO voices(id,name,created_at) VALUES (gen_random_uuid(),'Bombo',now());
