package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RolesRepository extends JpaRepository<Roles, UUID> {
}
