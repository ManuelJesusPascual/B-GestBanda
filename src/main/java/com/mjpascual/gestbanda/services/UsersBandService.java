package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.dto.usersBand.CreateUsersBandRequest;
import com.mjpascual.gestbanda.dto.usersBand.ModifyUsersBandRequest;
import com.mjpascual.gestbanda.dto.usersBand.UsersBandResponse;
import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import com.mjpascual.gestbanda.entities.UsersBand;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.UsersBandRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

/**
 * Service class responsible for managing user-band associations,
 * allowing users to be linked to specific bands with specific roles.
 */
@Service
public class UsersBandService {

    public static final String ROLE_DIRECTOR = "DIRECTOR";
    public static final String ROLE_VOICE_LEADER = "JEFE DE VOZ";
    public static final String ROLE_MEMBER = "COMPONENTE";

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

        return usersBandRepository.save(bandToSave);
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
     * Finds all users associated with a specific band ID.
     *
     * @param bandUuid Unique identifier of the band.
     * @return List of UsersBandResponse objects representing the user's band associations.
     */
    @Transactional(rollbackOn = Exception.class)
    public List<UsersBandResponse> findByBand(UUID bandUuid) {
        List<UsersBand> userInBand = usersBandRepository.findByBandID(bandUuid);
        return UsersBandResponse.from(userInBand);
    }

    /**
     * Inserts a new user-band relationship with the given role.
     *
     * @param band The saved band information.
     * @param voice The voice information associated with the user.
     * @param user The user to associate with the band.
     * @param roleName Name of the role the user gets in the band.
     */
    @Transactional(rollbackOn = Exception.class)
    public void insertUserBand(BandsResponse band, VoicesResponse voice, UsersResponse user, String roleName) {
        RolesResponse role = rolesService.searchRolByName(roleName);
        UsersBand userBandCheck = usersBandRepository.findByUserBandID(user.getId(), band.getId());

        if (!isNull(userBandCheck)) {
            throw new GestBandaException(new Exception(), "Este usuario ya se encuentra en esa banda", ErrorCodes.USER_ALREADY_IN_BAND);
        }

        CreateUsersBandRequest createUsersInBandRequest = new CreateUsersBandRequest(band, user, role, voice);
        createUsersInBandRequest.validate();

        createUserInBand(createUsersInBandRequest);
    }

    /**
     * Modifies an existing user-band relationship based on the provided request.
     *
     * @param request The request containing the modifications to be made.
     */
    @Transactional(rollbackOn = Exception.class)
    public void modifyUserBand(ModifyUsersBandRequest request) {
        usersBandRepository.updateUserVoice(request.getVoice_id(), request.getRole_id(), request.getUser_id(), request.getBand_id(), new Date());
    }

    /**
     * Deletes a user-band association based on the user ID and band ID.
     *
     * @param idUser Unique identifier of the user.
     * @param idBand Unique identifier of the band.
     */
    @Transactional(rollbackOn = Exception.class)
    public void deleteUserBand(UUID idUser, UUID idBand) {
        UsersBand userBand = usersBandRepository.findByUserBandID(idUser, idBand);
        usersBandRepository.delete(userBand);
    }
}
