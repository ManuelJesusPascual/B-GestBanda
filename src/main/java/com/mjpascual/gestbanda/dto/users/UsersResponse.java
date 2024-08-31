package com.mjpascual.gestbanda.dto.users;

import com.mjpascual.gestbanda.dto.userHasRoleInBand.UserHasRoleInBandResponse;
import com.mjpascual.gestbanda.dto.usersInBand.UsersInBandResponse;
import com.mjpascual.gestbanda.entities.UserGoTravel;
import com.mjpascual.gestbanda.entities.UserHasClothes;
import com.mjpascual.gestbanda.entities.UserHasInstruments;
import com.mjpascual.gestbanda.entities.Users;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UsersResponse {

    private UUID id;

    private String name;

    private String email;

    private String phone;

    private List<UsersInBandResponse> usersInBandResponse;

    private List<UserHasRoleInBandResponse> userHasRoleInBandResponse;

    private List<UserGoTravel> userGoTravel;

    private List<UserHasClothes> userHasClothes;

    private List<UserHasInstruments> userHasInstruments;

    public static UsersResponse from(Users user) {
        return UsersResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .usersInBandResponse(UsersInBandResponse.from(user.getUsersInBand()))
                .userHasRoleInBandResponse(UserHasRoleInBandResponse.from(user.getUserHasRoleInBand()))
                .userGoTravel(user.getUserGoTravel())
                .userHasClothes(user.getUserHasClothes())
                .userHasInstruments(user.getUserHasInstruments())
                .build();

    }

}
