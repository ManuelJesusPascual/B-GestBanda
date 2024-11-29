package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.BandVoices;
import com.mjpascual.gestbanda.entities.Codes;
import com.mjpascual.gestbanda.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CodesRepository extends JpaRepository<Codes, UUID> {

    @Query(value = "SELECT * FROM codes WHERE code = ?1", nativeQuery = true)
    public Codes findByCode(String code);

    @Query(value = "SELECT * FROM codes WHERE band_id = ?1", nativeQuery = true)
    public List<Codes> findBandCodes(UUID bandId);


    @Query(value = "SELECT * FROM codes WHERE id = ?1", nativeQuery = true)
    public Codes checkIfExist(UUID idCode);

    @Query(value = "SELECT * FROM codes WHERE code = ?1", nativeQuery = true)
    public Codes checkIfExistByCode(String code);

}
