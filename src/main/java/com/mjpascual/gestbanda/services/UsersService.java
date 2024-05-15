package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.entities.Users;
import com.mjpascual.gestbanda.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Método que crea un nuevo usuario
     * @return Usuario creado
     */

    @Transactional(rollbackOn = Exception.class)
    public Users createUser (Users user){
        //TODO: retorna la response no la entitie
        return usersRepository.save(user);

    }

}
