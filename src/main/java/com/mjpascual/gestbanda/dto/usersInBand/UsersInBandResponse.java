package com.mjpascual.gestbanda.dto.usersInBand;


import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.entities.Users;

import com.mjpascual.gestbanda.entities.UsersInBand;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UsersInBandResponse {

    private Bands band_id;

    private Users user_id;


public static UsersInBandResponse from(UsersInBand usersInBand) {

    return UsersInBandResponse.builder()
            .band_id(usersInBand.getBand_id())
            .user_id(usersInBand.getUser_id())
            .build();


}

    public static List<UsersInBandResponse> from(List<UsersInBand> usersInBand) {
        List<UsersInBandResponse> usersInBandResponseList = new ArrayList<>();
        for (UsersInBand iterator : usersInBand) {
            usersInBandResponseList.add(UsersInBandResponse.from(iterator));
        }
        return usersInBandResponseList;
    }
}
