package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.UserHasRoleInBand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleInBandRepository extends JpaRepository<UserHasRoleInBand, UUID> {
}
