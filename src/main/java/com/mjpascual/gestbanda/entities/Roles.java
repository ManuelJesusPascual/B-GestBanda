package com.mjpascual.gestbanda.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "roles")
public class Roles {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "route")
    private String route;

    @Column(name = "updated_at")
    private String updated_at;

    @Column(name = "created_at")
    private String created_at;

    @Transient
    private List<UserHasRoleInBand> userHasRoleInBand;


}
