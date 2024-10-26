package com.mjpascual.gestbanda.dto.usersBand;


import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;

import com.mjpascual.gestbanda.entities.UsersBand;
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
public class UsersBandResponse {

    private BandsResponse band;

    private UsersResponse user;

    private RolesResponse role;


    public static UsersBandResponse from(UsersBand usersBand) {

        return UsersBandResponse.builder()
                .band(BandsResponse.from(usersBand.getBand_id()))
                .user(UsersResponse.from(usersBand.getUser_id()))
                .role(RolesResponse.from(usersBand.getRol_id()))
                .build();


    }

    public static List<UsersBandResponse> from(List<UsersBand> usersBand) {

        List<UsersBandResponse> usersInBandResponseList = new ArrayList<>();
        if(!isNull(usersBand) && !usersBand.isEmpty()){
            for (UsersBand iterator : usersBand) {
                usersInBandResponseList.add(UsersBandResponse.from(iterator));
            }
        }
        return usersInBandResponseList;
    }
}
