package com.mjpascual.gestbanda.repositories;

import com.mjpascual.gestbanda.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {

    @Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
    public Users findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE phone = ?1", nativeQuery = true)
    public Users findByPhone(String phone);
}
