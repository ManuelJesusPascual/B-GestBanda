package com.mjpascual.gestbanda;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Starts the whole application against a real PostgreSQL (Testcontainers) so the
 * Liquibase changelog and the JPA mappings are checked together.
 */
@Testcontainers
@SpringBootTest(properties = "jwt.secret=test-secret-key-that-is-at-least-32-bytes-long")
class GestBandaApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Test
    void contextLoads() {
    }

}
