package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.usersBand.CreateUsersBandRequest;
import com.mjpascual.gestbanda.dto.usersBand.UsersBandResponse;
import com.mjpascual.gestbanda.entities.UsersBand;
import com.mjpascual.gestbanda.repositories.UsersBandRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    /**
     * Constructor for injecting UsersBandRepository dependency.
     *
     * @param usersBandRepository Repository for managing user-band relationships.
     */
    public UsersBandService(UsersBandRepository usersBandRepository) {
        this.usersBandRepository = usersBandRepository;
    }

    /**
     * Creates a new record in the UsersBand relationship, linking a user to a band.
     * Sets the creation date and saves the new user-band association.
     *
     * @param createUsersInBand Request object containing user-band association details.
     * @return UsersBandResponse representing the newly created user-band association.
     */
    @Transactional(rollbackOn = Exception.class)
    public UsersBandResponse createUserInBand(CreateUsersBandRequest createUsersInBand) {
        UsersBand bandToSave = UsersBand.from(createUsersInBand);
        bandToSave.setCreated_at(new Date());

        UsersBand userInBand = usersBandRepository.save(bandToSave);

        return UsersBandResponse.from(userInBand);
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
}
