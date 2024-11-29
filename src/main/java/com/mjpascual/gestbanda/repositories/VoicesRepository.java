package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Codes;
import com.mjpascual.gestbanda.entities.Roles;
import com.mjpascual.gestbanda.entities.Voices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface VoicesRepository extends JpaRepository<Voices, UUID> {
    @Query(value = "SELECT * FROM voices WHERE name = ?1", nativeQuery = true)
     Voices findVoiceByName(String name);

    @Query(value = "SELECT * FROM voices WHERE id NOT IN (SELECT id_voice FROM band_voices WHERE id_band = ?1)", nativeQuery = true)
     List<Voices> findVoicesBandNotHave(UUID id);

    @Query(value = "SELECT * FROM voices WHERE name = 'Sin Voz'", nativeQuery = true)
     Voices getNoVoice();

    @Modifying
    @Query(value = "UPDATE codes SET voice_id = ?1, updated_at = ?4 WHERE voice_id = ?2 AND band_id = ?3", nativeQuery = true)
     void setCodesVoicesToDefault(UUID idVozDefault, UUID idVozDelete, UUID idBand,Date updatedAt);


    @Modifying
    @Query(value = "UPDATE user_band SET voice_id = ?1,updated_at = ?4 WHERE voice_id = ?2 AND band_id = ?3", nativeQuery = true)
     void setUserVoicesToDefault(UUID idVozDefault, UUID idVozDelete, UUID idBand, Date updatedAt);



}
