package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.repositories.RolesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that handles operations related to roles.
 * This class interacts with the RolesRepository to perform CRUD operations.
 */
@Service
public class RolesService {

    private final RolesRepository rolesRepository;

    /**
     * Constructor that injects the RolesRepository dependency.
     * @param rolesRepository The repository used for role operations.
     */
    @Autowired
    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    /**
     * Method that searches for a role by its name.
     * @param name The name of the role to search for.
     * @return Obtained role.
     */
    @Transactional(rollbackOn = Exception.class)
    public RolesResponse searchRolByName(String name) {
        return RolesResponse.from(rolesRepository.findByName(name));
    }

    /**
     * Method that retrieves all roles from the repository.
     * @return List of all roles.
     */
    public List<RolesResponse> getAllRoles() {
        return RolesResponse.from(rolesRepository.findAll());
    }
}
