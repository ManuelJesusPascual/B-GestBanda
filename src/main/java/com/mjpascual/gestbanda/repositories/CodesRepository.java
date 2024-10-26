package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Codes;
import com.mjpascual.gestbanda.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CodesRepository extends JpaRepository<Codes, UUID> {

    @Query(value = "SELECT * FROM codes WHERE code = ?1", nativeQuery = true)
    public Codes findByCode(String code);
}
