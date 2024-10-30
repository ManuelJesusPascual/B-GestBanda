package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface RolesRepository extends JpaRepository<Roles, UUID> {

    @Query(value = "SELECT * FROM roles WHERE name = ?1", nativeQuery = true)
    public Roles findByName(String name);

}
