package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.UserHasInstruments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserInstrumentsRepository extends JpaRepository<UserHasInstruments, UUID> {
}
