package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.BandHasInstruments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BandInstrumentsRepository extends JpaRepository<BandHasInstruments, UUID> {


}
