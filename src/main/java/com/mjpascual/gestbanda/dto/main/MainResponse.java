package com.mjpascual.gestbanda.dto.main;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.userHasRoleInBand.UserHasRoleInBandResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.dto.usersInBand.UsersInBandResponse;
import com.mjpascual.gestbanda.entities.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MainResponse {

    private UUID id;

    private String name;

    private String email;

    private String phone;

    private BandsResponse band; //TODO: Ver como controlar el que como el usuario tiene varias bandas, tendrá  que tener una lista para seleccionar que banda quiere visualizar,
    // y este atributo se rellenará conforme a la banda que este seleccionada en esa lista, pero esa lista ¿Donde esta?
    // Porque usersInBand solo contiene el id de las bandas que esta este usuario, pero hace falta ni aunque sea un  nombrecito para ponerlo en la lista
    // Se me ocurre que cada vez que entre en esa lista de selección de banda haga una petición a la API para obtener una lista con los datos de las bandas que estan
    // En usersInBand y ahí mostrarlo en la lista, pero eso es peruanada de manual

    private List<UsersInBandResponse> usersInBandResponse;

    private List<UserHasRoleInBandResponse> userHasRoleInBandResponse;

    private List<UserGoTravel> userGoTravel;

    private List<UserHasClothes> userHasClothes;

    private List<UserHasInstruments> userHasInstruments;

    public static MainResponse from(UsersResponse user, BandsResponse band) {
        return MainResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .band(band)
                .usersInBandResponse(user.getUsersInBandResponse())
                .userHasRoleInBandResponse(user.getUserHasRoleInBandResponse())
                .userGoTravel(user.getUserGoTravel())
                .userHasClothes(user.getUserHasClothes())
                .userHasInstruments(user.getUserHasInstruments())
                .build();

    }

    public static MainResponse from(UsersResponse userResponse, BandsResponse bandResponse, UsersInBandResponse userInBandResponse, UserHasRoleInBandResponse userHasRoleInBandResponse) {

        List<UsersInBandResponse> userInBandResponseList = new ArrayList<>();
        userInBandResponseList.add(userInBandResponse);
        List<UserHasRoleInBandResponse> userHasRoleInBandResponseList = new ArrayList<>();
        userHasRoleInBandResponseList.add(userHasRoleInBandResponse);

        return MainResponse.builder()
                .id(userResponse.getId())
                .name(userResponse.getName())
                .email(userResponse.getEmail())
                .phone(userResponse.getPhone())
                .band(bandResponse)
                .usersInBandResponse(userInBandResponseList)
                .userHasRoleInBandResponse(userHasRoleInBandResponseList)
                .build();

    }

//TODO: Revisar esto despues de revisar el comentario de MainResponse y de StarterService




}
