package com.mjpascual.gestbanda.dto.roles;

import com.mjpascual.gestbanda.entities.Roles;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RolesResponse {

    private UUID id;
    private String name;
    private String router;
    private Date updated_at;
    private Date created_at;

    public static RolesResponse from(Roles rol) {
        return RolesResponse.builder()
                .id(rol.getId())
                .name(rol.getName())
                .router(rol.getRouter())
                .updated_at(rol.getUpdated_at())
                .created_at(rol.getCreated_at())
                .build();

    }
}


