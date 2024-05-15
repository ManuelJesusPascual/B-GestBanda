package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Bands;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Interfaz que define los métodos que se pueden realizar sobre la entidad Bands
 */
public interface BandsRepository extends JpaRepository<Bands, UUID> {

}
