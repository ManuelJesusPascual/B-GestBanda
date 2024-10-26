package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Users;
import com.mjpascual.gestbanda.entities.UsersBand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UsersBandRepository extends JpaRepository<UsersBand, UUID> {

    @Query(value = "SELECT * FROM user_band WHERE user_id = ?1", nativeQuery = true)
    public List<UsersBand> findByUserID(UUID userID);
}
