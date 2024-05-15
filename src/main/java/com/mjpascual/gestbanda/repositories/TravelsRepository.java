package com.mjpascual.gestbanda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TravelsRepository extends JpaRepository<TravelsRepository, UUID> {
}
