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
        // Inicializamos los mocks
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test que verifica que el método registerUser de UserController devuelve una respuesta 201 y llama al servicio
     * UsersService.createUser()
     */
    @Test
    void testRegisterUser() {
        // Datos de prueba
        CreateUserRequest request = new CreateUserRequest("Nombre", "email@example.com", "123456789", "Password1!");

        // Datos simulados para UsersResponse, usando los datos típicos que vendrían de la entidad Users
        UUID userId = UUID.randomUUID();
        UsersResponse usersResponse = UsersResponse.builder()
                .id(userId)
                .name("Nombre")
                .email("email@example.com")
                .phone("123456789")
                .usersInBandResponse(Collections.emptyList())
                .userHasRoleInBandResponse(Collections.emptyList())
                .userGoTravel(Collections.emptyList())
                .userHasClothes(Collections.emptyList())
                .userHasInstruments(Collections.emptyList())
                .build();

        // Configuramos el comportamiento del servicio mockeado
        when(usersService.createUser(request)).thenReturn(usersResponse);

        // Ejecutamos el método del controlador
        ResponseEntity<UsersResponse> responseEntity = userController.registerUser(request);

        // Verificamos que la respuesta sea la esperada
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(usersResponse, responseEntity.getBody());

        // Verificamos que se llamó al método createUser del servicio
        verify(usersService, times(1)).createUser(request);
    }

    /**
     * Test que comprobará que si todos los datos son correctos, pasa todas las validaciones sin lanzar excepciones
     */
    @Test
    void testValidateSuccess() {
        // Datos de prueba válidos
        CreateUserRequest request = new CreateUserRequest("Nombre", "email@example.com", "123456789", "Password1!");

        // Verificamos que no lanza ninguna excepción
        assertDoesNotThrow(request::validate);
    }

    /**
     * Test que verificará que se lanzan excepciones cuando algún campo es null
     */
    @Test
    void testValidateNullFields() {
        // Verificamos que se lanza una excepción si el nombre es nulo
        CreateUserRequest requestWithNullName = new CreateUserRequest(null, "email@example.com", "123456789", "Password1!");
        assertThrows(GestBandaException.class, requestWithNullName::validate);

        // Verificamos que se lanza una excepción si el email es nulo
        CreateUserRequest requestWithNullEmail = new CreateUserRequest("Nombre", null, "123456789", "Password1!");
        assertThrows(GestBandaException.class, requestWithNullEmail::validate);

        // Verificamos que se lanza una excepción si el teléfono es nulo
        CreateUserRequest requestWithNullPhone = new CreateUserRequest("Nombre", "email@example.com", null, "Password1!");
        assertThrows(GestBandaException.class, requestWithNullPhone::validate);

        // Verificamos que se lanza una excepción si la contraseña es nula
        CreateUserRequest requestWithNullPassword = new CreateUserRequest("Nombre", "email@example.com", "123456789", null);
        assertThrows(GestBandaException.class, requestWithNullPassword::validate);
    }

    /**
     * Test que verificará que se lanzan excepciones cuando algún campo no es válido
     */
    @Test
    void testValidateInvalidFields() {
        // Verificamos que se lanza una excepción si el nombre no es válido
        CreateUserRequest requestWithInvalidName = new CreateUserRequest("Nombre123", "email@example.com", "123456789", "Password1!");
        assertThrows(GestBandaException.class, requestWithInvalidName::validate);

        // Verificamos que se lanza una excepción si el email no es válido
        CreateUserRequest requestWithInvalidEmail = new CreateUserRequest("Nombre", "email@", "123456789", "Password1!");
        assertThrows(GestBandaException.class, requestWithInvalidEmail::validate);

        // Verificamos que se lanza una excepción si el teléfono no es válido
        CreateUserRequest requestWithInvalidPhone = new CreateUserRequest("Nombre", "email@example.com", "12345", "Password1!");
        assertThrows(GestBandaException.class, requestWithInvalidPhone::validate);

        // Verificamos que se lanza una excepción si la contraseña no es válida
        CreateUserRequest requestWithInvalidPassword = new CreateUserRequest("Nombre", "email@example.com", "123456789", "password");
        assertThrows(GestBandaException.class, requestWithInvalidPassword::validate);
    }
}
