package com.mjpascual.gestbanda.dto.users;

import com.mjpascual.gestbanda.entities.*;
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

    private  List<UsersInBand> usersInBand;

    private List<UserHasRoleInBand> userHasRoleInBand;


    private List<UserGoTravel> userGoTravel;

    private List<UserHasClothes> userHasClothes;

    private List<UserHasInstruments> userHasInstruments;

    public static UsersResponse from(Users user) {
        return UsersResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .usersInBand(user.getUsersInBand())
                .userHasRoleInBand(user.getUserHasRoleInBand())
                .userGoTravel(user.getUserGoTravel())
                .userHasClothes(user.getUserHasClothes())
                .userHasInstruments(user.getUserHasInstruments())
                .build();

    }

}
