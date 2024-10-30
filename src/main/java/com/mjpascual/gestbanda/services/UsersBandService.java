package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.dto.usersBand.CreateUsersBandRequest;
import com.mjpascual.gestbanda.dto.usersBand.UsersBandResponse;
import com.mjpascual.gestbanda.entities.UsersBand;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.UsersBandRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

/**
 * Service class responsible for managing user-band associations,
 * allowing users to be linked to specific bands with specific roles.
 */
@Service
public class UsersBandService {

    /**
     * Repository for performing CRUD operations on UsersBand entity.
     */
    private final UsersBandRepository usersBandRepository;

    private final RolesService rolesService;



    /**
     * Constructor for injecting UsersBandRepository dependency.
     *
     * @param usersBandRepository Repository for managing user-band relationships.
     */
    public UsersBandService(UsersBandRepository usersBandRepository, RolesService rolesService) {
        this.usersBandRepository = usersBandRepository;
        this.rolesService = rolesService;
    }

    /**
     * Creates a new record in the UsersBand relationship, linking a user to a band.
     * Sets the creation date and saves the new user-band association.
     *
     * @param createUsersInBand Request object containing user-band association details.
     * @return UsersBandResponse representing the newly created user-band association.
     */
    @Transactional(rollbackOn = Exception.class)
    public UsersBand createUserInBand(CreateUsersBandRequest createUsersInBand) {
        UsersBand bandToSave = UsersBand.from(createUsersInBand);

        UsersBand userInBand = usersBandRepository.save(bandToSave);

        return userInBand;
    }

    /**
     * Finds all bands associated with a specific user ID.
     *
     * @param uuid Unique identifier of the user.
     * @return List of UsersBandResponse objects representing the user's band associations.
     */
    @Transactional(rollbackOn = Exception.class)
    public List<UsersBandResponse> findByUser(UUID uuid) {
        List<UsersBand> userInBand = usersBandRepository.findByUserID(uuid);
        return UsersBandResponse.from(userInBand);
    }

    /**
     * Inserts a new user-band relationship, assigning the "DIRECTOR" role to the user.
     *
     * @param band The saved band information.
     * @param user The user to associate with the band.
     */
    public void insertUserBand(BandsResponse band, UsersResponse user) {
        RolesResponse rolDirector = rolesService.searchRolByName("DIRECTOR");
        UsersBand userBandCheck = usersBandRepository.findByUserBandID(user.getId(),band.getId());

        if(!isNull(userBandCheck)){
            throw new GestBandaException(new Exception(), "Este usuario ya se encuentra en esa banda", ErrorCodes.USER_ALREADY_IN_BAND);
        }

        CreateUsersBandRequest createUsersInBandRequest = new CreateUsersBandRequest(band, user, rolDirector);
        createUsersInBandRequest.validate();

        createUserInBand(createUsersInBandRequest);
    }

}
