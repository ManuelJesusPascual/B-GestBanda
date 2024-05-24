package com.mjpascual.gestbanda.entities;

import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.userHasRoleInBand.CreateUserHasRoleInBandRequest;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_has_role_in_band")
public class UserHasRoleInBand {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id")
    private Roles role_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id")
    private Bands band_id;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_at")
    private Date created_at;

    public static UserHasRoleInBand from(CreateUserHasRoleInBandRequest createUserHasRoleInBandRequest) {
        return UserHasRoleInBand.builder()
                .role_id(Roles.from(createUserHasRoleInBandRequest.getRole_id()))
                .user_id(createUserHasRoleInBandRequest.getUser_id())
                .band_id(createUserHasRoleInBandRequest.getBand_id())
                .updated_at(createUserHasRoleInBandRequest.getUpdated_at())
                .created_at(createUserHasRoleInBandRequest.getCreated_at())
                .build();

    }

}
