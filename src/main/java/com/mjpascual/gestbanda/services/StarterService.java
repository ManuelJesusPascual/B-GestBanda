package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.main.MainResponse;
import com.mjpascual.gestbanda.dto.starter.CreateStarterRequest;
import com.mjpascual.gestbanda.dto.userHasRoleInBand.CreateUserHasRoleInBandRequest;
import com.mjpascual.gestbanda.dto.userHasRoleInBand.UserHasRoleInBandResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.dto.usersInBand.UsersInBandResponse;
import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.entities.UserHasRoleInBand;
import com.mjpascual.gestbanda.entities.Users;
import com.mjpascual.gestbanda.entities.UsersInBand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class StarterService {

    private final BandsService bandsService;

    private final UsersService usersService;

    private final UsersBandService usersInBandService;

    private final UserRoleInBandService userRoleInBandService;

    private final RolesService rolesService;

    @Autowired
    public StarterService(BandsService bandsService, UsersService usersService, UsersBandService usersInBandService, UserRoleInBandService userRoleInBandService, RolesService rolesService) {
        this.bandsService = bandsService;
        this.usersService = usersService;
        this.usersInBandService = usersInBandService;
        this.userRoleInBandService = userRoleInBandService;
        this.rolesService = rolesService;
    }


    /**
     * Método que crea una nueva banda junto a su usuario
     *
     * @return Response principal
     */

    @Transactional(rollbackOn = Exception.class)
    public MainResponse createBand(CreateStarterRequest request) {
        Bands bandMapped = Bands.from(request.getBand());
        Users userMapped = Users.from(request.getUser());


        UsersInBand userInBand = UsersInBand.builder().id(null).band_id(bandMapped).user_id(userMapped).updated_at(null).created_at(new Date()).build();
        RolesResponse rolDirector = rolesService.searchRolByName("Director General");
        CreateUserHasRoleInBandRequest userHasRoleInBand = CreateUserHasRoleInBandRequest.builder().id(null).role_id(rolDirector).band_id(bandMapped).user_id(userMapped).updated_at(null).created_at(new Date()).build();
        //TODO: Se debe llamar al servicio que almacena las imagenes para que guarde la imagen de la banda y devuelva la url. O sino crearla con url de imagen a null y despues del pago actualizarla, asi te aseguras que no haya bandas sin pagar ocupando almacenamiento.
      
        return saveAll(bandMapped, userMapped, userInBand, userHasRoleInBand);

    }


    private MainResponse saveAll(Bands band, Users user, UsersInBand usersInBand, CreateUserHasRoleInBandRequest userHasRoleInBand) {
        band.setCreated_at(new Date());
        user.setCreated_at(new Date());

        BandsResponse bandSaved = bandsService.createBand(band); //SubscriptionID debe ser null porque la banda se crea antes de que se haya pagado la suscripción y despues de pagarla se actualiza.
        UsersResponse userSaved = usersService.createUser(user);

        UsersInBandResponse userInBandSavedResponse = usersInBandService.createUserInBand(usersInBand);
        UserHasRoleInBandResponse userHasRoleInBandSaved = userRoleInBandService.createUserRoleInBand(UserHasRoleInBand.from(userHasRoleInBand));

        // TODOSIR: Revisar comentario MainResponse para saber que hacer con estas listas, creo que si deben incluirse en el .from de MainResponse

        //Las añado y sino despues lo modifico

        return MainResponse.from(userSaved, bandSaved,userInBandSavedResponse,userHasRoleInBandSaved);
    }



}
