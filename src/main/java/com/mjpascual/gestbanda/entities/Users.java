package com.mjpascual.gestbanda.entities;

import com.mjpascual.gestbanda.dto.users.CreateUserRequest;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
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


    public static Users from(UsersResponse userDTO) {
        return Users.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .usersInBand(userDTO.getUsersInBand())
                .userHasRoleInBand(userDTO.getUserHasRoleInBand())
                .userGoTravel(userDTO.getUserGoTravel())
                .userHasClothes(userDTO.getUserHasClothes())
                .userHasInstruments(userDTO.getUserHasInstruments())
                .build();

    }

    public static Users from(CreateUserRequest createUserRequest) {
        return Users.builder()
                .name(createUserRequest.getName())
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword()) //TODO: Encriptar esto
                .phone(createUserRequest.getPhone())
                .build();

    }


}
