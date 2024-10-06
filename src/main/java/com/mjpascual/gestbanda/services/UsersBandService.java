package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.usersInBand.UsersInBandResponse;
import com.mjpascual.gestbanda.entities.UsersInBand;
import com.mjpascual.gestbanda.repositories.UsersBandRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UsersBandService {

    private final UsersBandRepository usersBandRepository;

    public UsersBandService(UsersBandRepository usersBandRepository) {
        this.usersBandRepository = usersBandRepository;
    }

    /**
     * Method that creates a new tuple in the UsersBands relationship.
     *
     * @return Created tuple.
     */
    @Transactional(rollbackOn = Exception.class)
    public UsersInBandResponse createUserInBand(UsersInBand userInBand) {
        return UsersInBandResponse.from(usersBandRepository.save(userInBand));
    }
}
