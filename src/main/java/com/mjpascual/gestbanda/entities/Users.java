package com.mjpascual.gestbanda.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_at")
    private Date created_at;

    @Transient
    private  List<UsersInBand> usersInBand;

    @Transient
    private List<UserHasRoleInBand> userHasRoleInBand;

    @Transient
    private List<UserGoTravel> userGoTravel;

    @Transient
    private List<UserHasClothes> userHasClothes;

    @Transient
    private List<UserHasInstruments> userHasInstruments;

}
