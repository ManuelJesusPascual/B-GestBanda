package com.mjpascual.gestbanda.users;

import com.mjpascual.gestbanda.controllers.UserController;
import com.mjpascual.gestbanda.dto.users.CreateUserRequest;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUserTest {

    @Mock
    private UsersService usersService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        // Initialize the mocks
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test to verify that the method registerUser of UserController returns a 201 response and calls the service
     * UsersService.createUser()
     */
    @Test
    void testRegisterUser() {
        // Test data
        CreateUserRequest request = new CreateUserRequest("Nombre", "email@example.com", "123456789", "Password1!");

        // Simulated data for UsersResponse, using typical data that would come from the Users entity
        UUID userId = UUID.randomUUID();
        UsersResponse usersResponse = UsersResponse.builder()
                .id(userId)
                .name("Nombre")
                .email("email@example.com")
                .phone("123456789")
                .build();

        // Configure the behavior of the mocked service
        when(usersService.createUser(request)).thenReturn(usersResponse);

        // Execute the controller method
        ResponseEntity<UsersResponse> responseEntity = userController.registerUser(request);

        // Verify that the response is as expected
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(usersResponse, responseEntity.getBody());

        // Verify that the createUser method of the service was called
        verify(usersService, times(1)).createUser(request);
    }

    /**
     * Test that checks if all data is correct, passing all validations without throwing exceptions
     */
    @Test
    void testValidateSuccess() {
        // Valid test data
        CreateUserRequest request = new CreateUserRequest("Nombre", "email@example.com", "123456789", "Password1!");

        // Verify that no exception is thrown
        assertDoesNotThrow(request::validate);
    }

    /**
     * Test that verifies exceptions are thrown when any field is null
     */
    @Test
    void testValidateNullFields() {
        // Verify that an exception is thrown if the name is null
        CreateUserRequest requestWithNullName = new CreateUserRequest(null, "email@example.com", "123456789", "Password1!");
        assertThrows(GestBandaException.class, requestWithNullName::validate);

        // Verify that an exception is thrown if the email is null
        CreateUserRequest requestWithNullEmail = new CreateUserRequest("Nombre", null, "123456789", "Password1!");
        assertThrows(GestBandaException.class, requestWithNullEmail::validate);

        // Verify that an exception is thrown if the phone is null
        CreateUserRequest requestWithNullPhone = new CreateUserRequest("Nombre", "email@example.com", null, "Password1!");
        assertThrows(GestBandaException.class, requestWithNullPhone::validate);

        // Verify that an exception is thrown if the password is null
        CreateUserRequest requestWithNullPassword = new CreateUserRequest("Nombre", "email@example.com", "123456789", null);
        assertThrows(GestBandaException.class, requestWithNullPassword::validate);
    }

    /**
     * Test that verifies exceptions are thrown when any field is invalid
     */
    @Test
    void testValidateInvalidFields() {
        // Verify that an exception is thrown if the name is invalid
        CreateUserRequest requestWithInvalidName = new CreateUserRequest("Nombre123", "email@example.com", "123456789", "Password1!");
        assertThrows(GestBandaException.class, requestWithInvalidName::validate);

        // Verify that an exception is thrown if the email is invalid
        CreateUserRequest requestWithInvalidEmail = new CreateUserRequest("Nombre", "email@", "123456789", "Password1!");
        assertThrows(GestBandaException.class, requestWithInvalidEmail::validate);

        // Verify that an exception is thrown if the phone is invalid
        CreateUserRequest requestWithInvalidPhone = new CreateUserRequest("Nombre", "email@example.com", "12345", "Password1!");
        assertThrows(GestBandaException.class, requestWithInvalidPhone::validate);

        // Verify that an exception is thrown if the password is invalid
        CreateUserRequest requestWithInvalidPassword = new CreateUserRequest("Nombre", "email@example.com", "123456789", "password");
        assertThrows(GestBandaException.class, requestWithInvalidPassword::validate);
    }
}
