package com.mjpascual.gestbanda.dto.userHasRoleInBand;

import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.entities.Roles;
import com.mjpascual.gestbanda.entities.UserHasRoleInBand;
import com.mjpascual.gestbanda.entities.Users;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserHasRoleInBandResponse {

    private Roles role_id;

    private Users user_id;

    private Bands band_id;


    public static UserHasRoleInBandResponse from(UserHasRoleInBand userHasRoleInBand) {

        return UserHasRoleInBandResponse.builder()
                .role_id(userHasRoleInBand.getRole_id())
                .user_id(userHasRoleInBand.getUser_id())
                .band_id(userHasRoleInBand.getBand_id())
                .build();

    }

    ;

    public static List<UserHasRoleInBandResponse> from(List<UserHasRoleInBand> userHasRoleInBand) {
        List<UserHasRoleInBandResponse> userHasRoleInBandResponseList = new ArrayList<>();
        if(!isNull(userHasRoleInBand) && !userHasRoleInBand.isEmpty()) {

            for (UserHasRoleInBand iterator : userHasRoleInBand) {
                userHasRoleInBandResponseList.add(UserHasRoleInBandResponse.from(iterator));
            }
        }
        return userHasRoleInBandResponseList;
    }

}

