package com.mjpascual.gestbanda.dto.main;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
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

    private List<UsersInBand> usersInBand;

    private List<UserHasRoleInBand> userHasRoleInBand;

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
                .usersInBand(user.getUsersInBand())
                .userHasRoleInBand(user.getUserHasRoleInBand())
                .userGoTravel(user.getUserGoTravel())
                .userHasClothes(user.getUserHasClothes())
                .userHasInstruments(user.getUserHasInstruments())
                .build();

    }

//TODO: Revisar esto despues de revisar el comentario de MainRepsonse y de StarterService

    public static MainResponse from(UsersResponse user, BandsResponse band, UsersInBand usersInBand, UserHasRoleInBand userHasRoleInBand) {
        List<UsersInBand> usersInBandList = new ArrayList<>();
        usersInBandList.add(usersInBand);
        List<UserHasRoleInBand> userHasRoleInBandList = new ArrayList<>();
        userHasRoleInBandList.add(userHasRoleInBand);


        return MainResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .band(band)
                .usersInBand(usersInBandList)
                .userHasRoleInBand(userHasRoleInBandList)
                .userGoTravel(user.getUserGoTravel())
                .userHasClothes(user.getUserHasClothes())
                .userHasInstruments(user.getUserHasInstruments())
                .build();

    }


}
