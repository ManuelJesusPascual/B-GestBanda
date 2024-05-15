package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.UserHasClothes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserClothesRepository extends JpaRepository<UserHasClothes, UUID> {
}
