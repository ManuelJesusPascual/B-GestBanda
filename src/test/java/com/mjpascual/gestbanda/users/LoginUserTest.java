package com.mjpascual.gestbanda.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjpascual.gestbanda.config.Security.AuthCredentials;
import com.mjpascual.gestbanda.config.Security.JWTAuthenticationFilter;
import com.mjpascual.gestbanda.config.Security.UserDetailsImpl;
import com.mjpascual.gestbanda.entities.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para JWTAuthenticationFilter
 * Esta clase contiene pruebas para verificar el comportamiento del filtro de autenticación JWT
 */
class LoginUserTest {

    // Declaración del filtro que vamos a probar
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    // Mock del AuthenticationManager que usará el filtro
    @Mock
    private AuthenticationManager authenticationManager;

    /**
     * Método de configuración que se ejecuta antes de cada prueba
     * Inicializa los mocks y crea una nueva instancia del filtro
     */
    @BeforeEach
    void setUp() {
        // Inicializa los mocks anotados en esta clase
        MockitoAnnotations.openMocks(this);
        // Crea una nueva instancia del filtro
        jwtAuthenticationFilter = new JWTAuthenticationFilter();
        // Establece el AuthenticationManager mock en el filtro
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
    }

    /**
     * Prueba el login con credenciales válidas
     * Verifica que se devuelve un objeto Authentication cuando las credenciales son correctas
     */
    @Test
    void attemptAuthentication_ValidCredentials_ReturnsAuthentication() throws IOException {
        // Preparación
        // Crea objetos mock para la petición y respuesta HTTP
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Crea un objeto de credenciales con email y contraseña
        AuthCredentials credentials = new AuthCredentials();
        credentials.setEmail("test@example.com");
        credentials.setPassword("password");

        // Convierte las credenciales a JSON y las establece como contenido de la petición
        String jsonCredentials = new ObjectMapper().writeValueAsString(credentials);
        request.setContent(jsonCredentials.getBytes());

        // Crea un usuario y un UserDetails para simular un usuario autenticado
        Users user = new Users();
        user.setEmail("test@example.com");
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication mockedAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Configura el mock del AuthenticationManager para que devuelva la autenticación simulada
        when(authenticationManager.authenticate(any())).thenReturn(mockedAuthentication);

        // Ejecución
        // Llama al método attemptAuthentication del filtro
        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        // Verificación
        // Comprueba que el resultado no es nulo y es igual a la autenticación simulada
        assertNotNull(result);
        assertEquals(mockedAuthentication, result);
        // Verifica que se llamó al método authenticate del AuthenticationManager
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    /**
     * Prueba el proceso de login con credenciales inválidas
     * Verifica que se lanza una AuthenticationException cuando las credenciales son incorrectas
     */
    @Test
    void attemptAuthentication_InvalidCredentials_ThrowsAuthenticationException() throws IOException {
        // Preparación
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Crea credenciales con una contraseña incorrecta
        AuthCredentials credentials = new AuthCredentials();
        credentials.setEmail("test@example.com");
        credentials.setPassword("wrongpassword");

        String jsonCredentials = new ObjectMapper().writeValueAsString(credentials);
        request.setContent(jsonCredentials.getBytes());

        // Configura el mock para que lance una excepción al autenticar
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationException("Invalid credentials") {});

        // Ejecución y Verificación
        // Comprueba que se lanza una AuthenticationException al intentar autenticar
        assertThrows(AuthenticationException.class, () -> {
            jwtAuthenticationFilter.attemptAuthentication(request, response);
        });
    }

    /**
     * Prueba el proceso de autenticación con JSON inválido
     * Verifica que se intenta autenticar con credenciales nulas y se lanza una excepción
     */
    @Test
    void attemptAuthentication_InvalidJson_AttemptsAuthenticationWithNullCredentials() throws IOException {
        // Preparación
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        // Establece un JSON inválido como contenido de la petición
        request.setContent("invalid json".getBytes());

        // Configura el mock para que lance una excepción al autenticar
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationException("Invalid credentials") {});

        // Ejecución y Verificación
        // Comprueba que se lanza una AuthenticationException al intentar autenticar
        assertThrows(AuthenticationException.class, () -> {
            jwtAuthenticationFilter.attemptAuthentication(request, response);
        });

        // Verifica que se llamó a authenticate con un token que tiene credenciales nulas
        verify(authenticationManager).authenticate(argThat(auth ->
                auth instanceof UsernamePasswordAuthenticationToken
                        && auth.getPrincipal() == null
                        && auth.getCredentials() == null
        ));
    }
}