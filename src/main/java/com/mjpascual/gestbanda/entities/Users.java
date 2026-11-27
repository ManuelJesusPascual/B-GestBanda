package com.mjpascual.gestbanda.entities;

import com.mjpascual.gestbanda.dto.users.CreateUserRequest;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    private List<UsersBand> usersBand;


    public static Users from(UsersResponse userDTO) {
        return Users.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .email(userDTO.getEmail().toLowerCase())
                .phone(userDTO.getPhone())
                .build();

    }

    public static Users from(CreateUserRequest createUserRequest) {
        return Users.builder()
                .name(createUserRequest.getName())
                .email(createUserRequest.getEmail().toLowerCase())
                .password(createUserRequest.getPassword())
                .phone(createUserRequest.getPhone())
                .usersBand(new ArrayList<>())
                .created_at(new Date())
                .build();

    }


}
