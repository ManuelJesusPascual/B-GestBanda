package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Instruments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstrumentsRepository extends JpaRepository<Instruments, UUID> {
}
