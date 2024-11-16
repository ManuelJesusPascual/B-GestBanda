package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Codes;
import com.mjpascual.gestbanda.entities.Roles;
import com.mjpascual.gestbanda.entities.Voices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface VoicesRepository extends JpaRepository<Voices, UUID> {
    @Query(value = "SELECT * FROM voices WHERE name = ?1", nativeQuery = true)
    public Voices findVoiceByName(String name);

    @Query(value = "SELECT * FROM voices WHERE id NOT IN (SELECT id_voice FROM band_voices WHERE id_band = ?1)", nativeQuery = true)
    public List<Voices> findVoicesBandNotHave(UUID id);
}
