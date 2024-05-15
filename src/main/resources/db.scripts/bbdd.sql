
CREATE TABLE bands(

                        id uuid,
                        name varchar(120) NOT NULL,
                        img varchar(255),
                        subscription_id varchar(255) NOT NULL,
                        subscription_end timestamp,
                        updated_at timestamp,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_BAND PRIMARY KEY (id)

);

CREATE TABLE users(

                         id uuid,
                         name varchar(60) NOT NULL,
                         email varchar(50) NOT NULL UNIQUE,
                         password varchar(255) NOT NULL,
                         phone varchar(50) NOT NULL UNIQUE,
                         updated_at timestamp,
                         created_at timestamp NOT NULL,
                         CONSTRAINT PK_USERS PRIMARY KEY (id)

);

CREATE TABLE users_in_band(

                        id uuid,
                        band_id uuid NOT NULL,
                        user_id uuid NOT NULL,
                        updated_at timestamp,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_USERS_IN_BAND PRIMARY KEY (id),
                        CONSTRAINT FK_USERS_IN_BAND_BAND FOREIGN KEY (band_id) REFERENCES bands(id),
                        CONSTRAINT FK_USERS_IN_BAND_USER FOREIGN KEY (user_id) REFERENCES users(id),
                        CONSTRAINT UNIQUE_USERS_IN_BAND UNIQUE (band_id,user_id)

);

CREATE INDEX I_users_in_band_band ON users_in_band(band_id);
CREATE INDEX I_users_in_band_user ON users_in_band(user_id);


CREATE TABLE roles(

                        id uuid,
                        name varchar(90) NOT NULL UNIQUE,
                        route varchar(180) NOT NULL,
                        updated_at timestamp,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_ROLES PRIMARY KEY (id)
);

CREATE TABLE user_has_role_in_band(
                        id uuid,
                        rol_id uuid NOT NULL,
                        user_id uuid NOT NULL,
                        band_id uuid NOT NULL,
                        updated_at timestamp ,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_USER_HAS_ROLE_IN_BAND PRIMARY KEY (id),
                        CONSTRAINT FK_USER_HAS_ROLE_IN_BAND_ROL FOREIGN KEY (rol_id) references roles(id),
                        CONSTRAINT FK_USER_HAS_ROLE_IN_BAND_USER FOREIGN KEY (user_id) references users(id),
                        CONSTRAINT FK_USER_HAS_ROLE_IN_BAND_BAND FOREIGN KEY (band_id) references bands(id),
                        CONSTRAINT UNIQUE_USER_HAS_ROLE_IN_BAND UNIQUE (rol_id,user_id,band_id)

);

CREATE INDEX I_user_has_role_in_band_rol ON user_has_role_in_band(rol_id);
CREATE INDEX I_user_has_role_in_band_user ON user_has_role_in_band(user_id);
CREATE INDEX I_user_has_role_in_band_band ON user_has_role_in_band(band_id);


CREATE TABLE codes(

                        id uuid,
                        code varchar(15) NOT NULL UNIQUE,
                        end_date timestamp NOT NULL,
                        band_id uuid NOT NULL,
                        updated_at timestamp,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_CODE PRIMARY KEY(id),
                        CONSTRAINT FK_CODE_BAND FOREIGN KEY (band_id) REFERENCES bands(id)
);

CREATE INDEX I_codes_band ON codes(band_id);

CREATE TABLE instruments(

                        id uuid,
                        name varchar(50) NOT NULL UNIQUE,
                        img varchar(255) NOT NULL UNIQUE,
                        updated_at timestamp ,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_INSTRUMENTS PRIMARY KEY (id)

);

CREATE TABLE band_has_instruments(

                        id uuid,
                        band_id uuid NOT NULL,
                        instrument_id uuid NOT NULL,
                        quantity int NOT NULL,
                        updated_at timestamp,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_BAND_HAS_INSTRUMENTS PRIMARY KEY (id),
                        CONSTRAINT FK_BAND_HAS_INSTRUMENTS_BAND FOREIGN KEY (band_id) REFERENCES bands(id),
                        CONSTRAINT FK_BAND_HAS_INSTRUMENTS_INSTRUMENTS FOREIGN KEY (instrument_id) REFERENCES instruments(id),
                        CONSTRAINT UNIQUE_BAND_HAS_INSTRUMENTS UNIQUE (band_id,instrument_id)

);

CREATE INDEX I_band_has_instruments_band ON band_has_instruments(band_id);
CREATE INDEX I_band_has_instruments_instrument ON band_has_instruments(instrument_id);

CREATE TABLE user_has_instruments(

                        id uuid,
                        user_id uuid NOT NULL,
                        instrument_id uuid NOT NULL,
                        owner boolean NOT NULL,
                        quantity int NOT NULL,
                        updated_at timestamp,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_USER_HAS_INSTRUMENTS PRIMARY KEY (id),
                        CONSTRAINT FK_USER_HAS_INSTRUMENTS_USER FOREIGN KEY (user_id) REFERENCES users(id),
                        CONSTRAINT FK_USER_HAS_INSTRUMENTS_INSTRUMENT FOREIGN KEY (instrument_id) REFERENCES instruments(id),
                        CONSTRAINT UNIQUE_USER_HAS_INSTRUMENTS UNIQUE (user_id,instrument_id)

);

CREATE INDEX I_user_has_instruments_user ON user_has_instruments(user_id);
CREATE INDEX I_user_has_instruments_instrument ON user_has_instruments(instrument_id);

CREATE TABLE clothes(

                        id uuid,
                        name varchar(50) NOT NULL UNIQUE,
                        img varchar(255) NOT NULL UNIQUE,
                        updated_at timestamp ,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_CLOTHES PRIMARY KEY (id)

);

CREATE TABLE band_has_clothes(

                        id uuid,
                        band_id uuid NOT NULL,
                        clothes_id uuid NOT NULL,
                        quantity int NOT NULL,
                        updated_at timestamp ,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_BAND_HAS_CLOTHES PRIMARY KEY (id),
                        CONSTRAINT FK_BAND_HAS_CLOTHES_BAND FOREIGN KEY (band_id) REFERENCES bands(id),
                        CONSTRAINT FK_BAND_HAS_CLOTHES_CLOTHES FOREIGN KEY (clothes_id) REFERENCES clothes(id),
                        CONSTRAINT UNIQUE_BAND_HAS_CLOTHES UNIQUE (band_id,clothes_id)


);

CREATE INDEX I_band_has_clothes_band ON band_has_clothes(band_id);
CREATE INDEX I_band_has_clothes_clothes ON band_has_clothes(clothes_id);


CREATE TABLE user_has_clothes(

                        id uuid,
                        user_id uuid NOT NULL,
                        clothes_id uuid NOT NULL,
                        owner boolean NOT NULL,
                        quantity int NOT NULL,
                        updated_at timestamp,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_USER_HAS_CLOTHES PRIMARY KEY (id),
                        CONSTRAINT FK_USER_HAS_CLOTHES_USER FOREIGN KEY (user_id) REFERENCES users(id),
                        CONSTRAINT FK_USER_HAS_CLOTHES_INSTRUMENT FOREIGN KEY (clothes_id) REFERENCES clothes(id),
                        CONSTRAINT UNIQUE_USER_HAS_CLOTHES UNIQUE (user_id,clothes_id)

);

CREATE INDEX I_user_has_clothes_user ON user_has_clothes(user_id);
CREATE INDEX I_user_has_clothes_clothes ON user_has_clothes(clothes_id);


CREATE TABLE travels(

                        id uuid,
                        name varchar(50) NOT NULL,
                        description varchar(255) NOT NULL,
                        img varchar(255),
                        hourHQ timestamp,
                        hourPlace timestamp NOT NULL,
                        latitude double precision NOT NULL,
                        longitude double precision NOT NULL,
                        bus_offer boolean NOT NULL,
                        band_id uuid NOT NULL,
                        updated_at timestamp,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_TRAVELS PRIMARY KEY (id),
                        CONSTRAINT FK_TRAVELS_BAND FOREIGN KEY (band_id) REFERENCES bands(id)


);

CREATE INDEX I_travels_bands ON travels(band_id);

CREATE TABLE user_go_travel(

                        id uuid,
                        travel_id uuid NOT NULL,
                        user_id uuid NOT NULL,
                        user_go_on_bus boolean NOT NULL,
                        updated_at timestamp ,
                        created_at timestamp NOT NULL,
                        CONSTRAINT PK_USER_GO_TRAVEL PRIMARY KEY (id),
                        CONSTRAINT FK_USER_GO_TRAVEL_TRAVEL FOREIGN KEY (travel_id) REFERENCES travels(id),
                        CONSTRAINT FK_USER_GO_TRAVEL_USER FOREIGN KEY (user_id) REFERENCES users(id),
                        CONSTRAINT UNIQUE_USER_GO_TRAVEL UNIQUE(travel_id,user_id)

);

CREATE INDEX I_user_go_travel_travel ON user_go_travel(travel_id);
CREATE INDEX I_user_go_travel_user ON user_go_travel(user_id);



