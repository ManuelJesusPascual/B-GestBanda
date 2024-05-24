package com.mjpascual.gestbanda.dto.userHasRoleInBand;

import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.entities.Roles;
import com.mjpascual.gestbanda.entities.Users;
import lombok.*;

import java.util.Date;
import java.util.UUID;

//TODOSIR: No se pa que cojones tengo que hacer esto si tiene los mismos datos que la entidad pero weno, en este caso lo hago solamente
// para pasarle RolesResponse en vez de Roles,pero que pasa lo mismo con RolesResponse, si tiene los mismos datos de la entidad


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateUserHasRoleInBandRequest {


    private UUID id;

    private RolesResponse role_id;

    private Users user_id;

    private Bands band_id;

    private Date updated_at;

    private Date created_at;

}
