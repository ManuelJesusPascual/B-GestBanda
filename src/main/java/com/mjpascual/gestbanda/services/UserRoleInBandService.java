package com.mjpascual.gestbanda.services;

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
     * Método que crea una nueva tupla en la relacion de userRoleInBand
     * @return UserRoleInBands creado
     */

    @Transactional(rollbackOn = Exception.class)
    public UserHasRoleInBand createUserRoleInBand (UserHasRoleInBand userRoleInBand){

        return userRoleInBandRepository.save(userRoleInBand);

    }


}
