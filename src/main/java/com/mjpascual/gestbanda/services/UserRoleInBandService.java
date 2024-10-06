package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.userHasRoleInBand.UserHasRoleInBandResponse;
import com.mjpascual.gestbanda.entities.UserHasRoleInBand;
import com.mjpascual.gestbanda.repositories.UserRoleInBandRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserRoleInBandService {

    private final UserRoleInBandRepository userRoleInBandRepository;

    public UserRoleInBandService(UserRoleInBandRepository userRoleInBandRepository) {
        this.userRoleInBandRepository = userRoleInBandRepository;
    }

    /**
     * Method that creates a new tuple in the userRoleInBand relationship.
     *
     * @return Created UserRoleInBand.
     */
    @Transactional(rollbackOn = Exception.class)
    public UserHasRoleInBandResponse createUserRoleInBand(UserHasRoleInBand userRoleInBand) {
        return UserHasRoleInBandResponse.from(userRoleInBandRepository.save(userRoleInBand));
    }
}
