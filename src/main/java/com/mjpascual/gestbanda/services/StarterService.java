package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.bands.CreateBandRequest;
import com.mjpascual.gestbanda.dto.main.MainResponse;
import com.mjpascual.gestbanda.dto.starter.CreateStarterRequest;
import com.mjpascual.gestbanda.dto.users.CreateUserRequest;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.entities.UserHasRoleInBand;
import com.mjpascual.gestbanda.entities.Users;
import com.mjpascual.gestbanda.entities.UsersInBand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StarterService {

    private final BandsService bandsService;

    private final UsersService usersService;

    private final UsersBandService usersInBandService;

    private final UserRoleInBandService userRoleInBandService;

    @Autowired
    public StarterService(BandsService bandsService, UsersService usersService, UsersBandService usersInBandService, UserRoleInBandService userRoleInBandService) {
        this.bandsService = bandsService;
        this.usersService = usersService;
        this.usersInBandService = usersInBandService;
        this.userRoleInBandService = userRoleInBandService;
    }


    /**
     * Método que crea una nueva banda
     *
     * @return Banda creada
     */

    @Transactional(rollbackOn = Exception.class)
    public MainResponse createBand(CreateStarterRequest request) {
        CreateBandRequest band = request.getBand();
        CreateUserRequest user = request.getUser();
        Bands bandMapped = Bands.from(band); //TODO: Se podria hacer request.getBand y listo y abajo request.getUser ((  Bands bandMapped = Bands.from(request.getBand);  Yep eso es correcto))
        Users userMapped = Users.from(user);
        UsersInBand userInBand = UsersInBand.builder().id(null).band_id(bandMapped).user_id(userMapped).updated_at(null).created_at(new Date()).build();
        UserHasRoleInBand userHasRoleInBand = UserHasRoleInBand.builder().id(null).role_id(null).band_id(bandMapped).user_id(userMapped).updated_at(null).created_at(new Date()).build();

        //Se debe llamar al servicio que almacena las imagenes para que guarde la imagen de la banda y devuelva la url. O sino crearla con url de imagen a null y despues del pago actualizarla, asi te aseguras que no haya bandas sin pagar ocupando almacenamiento.


        return saveAll(bandMapped, userMapped, userInBand, userHasRoleInBand);


    }


    private MainResponse saveAll(Bands band, Users user, UsersInBand usersInBand, UserHasRoleInBand userHasRoleInBand) {
        Bands bandSaved = bandsService.createBand(band); //SubscriptionID debe ser null porque la banda se crea antes de que se haya pagado la suscripción y despues de pagarla se actualiza.
        Users userSaved = usersService.createUser(user);
        /**
         * TODO: ¿Se debe llamar antes a band o a user? Depende de la logica de dependencia de la base de datos si el usuario necesita una banda necesitas primero crear la banda y luego el usuario
         */
        UsersInBand userInBandSaved = usersInBandService.createUserInBand(usersInBand);
        UserHasRoleInBand userHasRoleInBandSaved = userRoleInBandService.createUserRoleInBand(userHasRoleInBand);

        /**
         * TODO: Intuyo que las listas no hace falta devolverlas en una primera instancia sino que después se hace una llamada a ese repo y ya ( Esto debe de devolver un objeto request y no una lista )
         */
        return generateResponse(bandSaved, userSaved);
    }


    private MainResponse generateResponse(Bands band, Users user) {

        UsersResponse userResponse = UsersResponse.from(user);
        BandsResponse bandResponse = BandsResponse.from(band);

        MainResponse mainResponse = MainResponse.builder().band(bandResponse).user(userResponse).build();
        return mainResponse;

    }


}
