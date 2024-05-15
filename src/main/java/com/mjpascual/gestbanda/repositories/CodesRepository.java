package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Codes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CodesRepository extends JpaRepository<Codes, UUID> {
}
