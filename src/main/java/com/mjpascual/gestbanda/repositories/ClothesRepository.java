package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClothesRepository extends JpaRepository<Clothes, UUID> {
}
