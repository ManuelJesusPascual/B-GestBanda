package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Travels;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TravelsRepository extends JpaRepository<Travels, UUID> {
}
