package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.users.UsersResponse;
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
    public UsersResponse createUser (Users user){

        return UsersResponse.from(usersRepository.save(user));
    }

}
