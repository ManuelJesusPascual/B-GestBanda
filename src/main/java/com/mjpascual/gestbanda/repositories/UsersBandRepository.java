package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.UsersInBand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersBandRepository extends JpaRepository<UsersInBand, UUID> {
}
