package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.UsersBand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersBandRepository extends JpaRepository<UsersBand, UUID> {
}
