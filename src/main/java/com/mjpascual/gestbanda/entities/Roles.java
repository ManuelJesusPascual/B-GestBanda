package com.mjpascual.gestbanda.entities;


import com.mjpascual.gestbanda.dto.roles.RolesResponse;
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

    @Column(name = "router")
    private String router;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_at")
    private Date created_at;


    public static Roles from(RolesResponse rol) {
        return Roles.builder()
                .id(rol.getId())
                .name(rol.getName())
                .router(rol.getRouter())
                .updated_at(rol.getUpdated_at())
                .created_at(rol.getCreated_at())
                .build();

    }

}
