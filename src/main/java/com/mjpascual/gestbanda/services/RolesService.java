package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.repositories.RolesRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;

    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    /**
     * Método que busca un rol por su nombre
     *
     * @return Rol obtenido
     */

    @Transactional(rollbackOn = Exception.class)
    public RolesResponse searchRolByName(String name) {
        return RolesResponse.from(rolesRepository.findByName(name));
    }

}
