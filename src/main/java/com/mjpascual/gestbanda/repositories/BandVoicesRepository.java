package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.dto.usersBand.UsersBandResponse;
import com.mjpascual.gestbanda.entities.BandVoices;
import com.mjpascual.gestbanda.entities.Roles;
import com.mjpascual.gestbanda.entities.Voices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

public interface BandVoicesRepository extends JpaRepository<BandVoices, UUID> {
    @Query(value = "SELECT * FROM band_voices WHERE id_band = ?1", nativeQuery = true)
    public List<BandVoices> findAllBandVoices(UUID id);




    @Query(value = "SELECT * FROM band_voices WHERE id_voice = ?1 AND id_band = ?2", nativeQuery = true)
    public BandVoices checkIfExist(UUID idVoice,UUID idBand);


}
