package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Bands;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Interface that defines the methods that can be performed on the Bands entity.
 */
public interface BandsRepository extends JpaRepository<Bands, UUID> {

}
