package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.BandHasClothes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BandClothesRepositories extends JpaRepository<BandHasClothes, UUID> {
}
