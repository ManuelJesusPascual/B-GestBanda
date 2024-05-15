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
public class UsersDTO {

    private UUID id;

    private String name;

    private String email;

    private String phone;

    private  List<UsersInBand> usersInBand;

    private List<UserHasRoleInBand> userHasRoleInBand;


    private List<UserGoTravel> userGoTravel;

    private List<UserHasClothes> userHasClothes;

    private List<UserHasInstruments> userHasInstruments;

}
