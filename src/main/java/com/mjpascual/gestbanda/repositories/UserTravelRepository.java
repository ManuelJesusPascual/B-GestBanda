package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.UserGoTravel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserTravelRepository extends JpaRepository<UserGoTravel, UUID> {
}
