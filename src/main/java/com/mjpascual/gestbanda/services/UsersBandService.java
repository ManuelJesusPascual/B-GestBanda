package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.entities.Users;
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
     * Método que crea una nueva tupla en la relación UsersBands
     * @return Tupla creada ( Esto te lo ha generado IA verdad? xD)
     */

    @Transactional(rollbackOn = Exception.class)
    public UsersInBand createUserInBand (UsersInBand userInBand){
        //TODO: retorna la response no la entitie
        return usersBandRepository.save(userInBand);

    }

}
