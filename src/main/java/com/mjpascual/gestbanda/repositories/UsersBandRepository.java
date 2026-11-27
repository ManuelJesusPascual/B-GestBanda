package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Users;
import com.mjpascual.gestbanda.entities.UsersBand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface UsersBandRepository extends JpaRepository<UsersBand, UUID> {

    @Query(value = "SELECT * FROM user_band WHERE user_id = ?1", nativeQuery = true)
    public List<UsersBand> findByUserID(UUID userID);

    @Query(value = "SELECT * FROM user_band WHERE user_id = ?1 AND band_id = ?2", nativeQuery = true)
    public UsersBand findByUserBandID(UUID userID,UUID bandID);

    @Query(value = "SELECT * FROM user_band WHERE band_id = ?1", nativeQuery = true)
    public List<UsersBand> findByBandID(UUID bandID);

    @Modifying
    @Query(value = "UPDATE user_band SET voice_id = ?1,rol_id = ?2,updated_at = ?5 WHERE user_id = ?3 AND band_id = ?4", nativeQuery = true)
    void updateUserVoice(UUID voiceID, UUID roleID, UUID userID, UUID bandID, Date updatedAt);
}
