package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.repositories.RolesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;

    @Autowired
    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    /**
     * Method that searches for a role by its name.
     *
     * @return Obtained role.
     */
    @Transactional(rollbackOn = Exception.class)
    public RolesResponse searchRolByName(String name) {
        return RolesResponse.from(rolesRepository.findByName(name));
    }
}
