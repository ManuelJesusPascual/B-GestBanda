package com.mjpascual.gestbanda.users;

import tools.jackson.databind.ObjectMapper;
import com.mjpascual.gestbanda.config.Security.AuthCredentials;
import com.mjpascual.gestbanda.config.Security.JWTAuthenticationFilter;
import com.mjpascual.gestbanda.config.Security.TokenUtils;
import com.mjpascual.gestbanda.config.Security.UserDetailsImpl;
import com.mjpascual.gestbanda.entities.Users;
import com.mjpascual.gestbanda.services.UsersService;
import jakarta.servlet.http.HttpServletResponse;
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
 * Unit test class for JWTAuthenticationFilter
 * This class contains tests to verify the behavior of the JWT authentication filter.
 */
class LoginUserTest {

    // Declaration of the filter to be tested
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    // Mock of the AuthenticationManager that the filter will use
    @Mock
    private AuthenticationManager authenticationManager;

    // Mock of the UsersService that the filter will use
    @Mock
    private UsersService usersService;

    /**
     * Setup method that runs before each test
     * Initializes mocks and creates a new instance of the filter
     */
    @BeforeEach
    void setUp() {
        // Initialize the mocks annotated in this class
        MockitoAnnotations.openMocks(this);
        // Create a new instance of the filter
        jwtAuthenticationFilter = new JWTAuthenticationFilter(usersService, new TokenUtils("test-secret-key-that-is-at-least-32-bytes-long"), new ObjectMapper());
        // Set the mock AuthenticationManager in the filter
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
    }

    /**
     * Test login with valid credentials
     * Verifies that an Authentication object is returned when the credentials are correct
     */
    @Test
    void attemptAuthentication_ValidCredentials_ReturnsAuthentication() throws IOException {
        // Preparation
        // Create mock objects for the HTTP request and response
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Create a credentials object with email and password
        AuthCredentials credentials = new AuthCredentials();
        credentials.setEmail("test@example.com");
        credentials.setPassword("password");

        // Convert the credentials to JSON and set them as request content
        String jsonCredentials = new ObjectMapper().writeValueAsString(credentials);
        request.setContent(jsonCredentials.getBytes());

        // Create a user and UserDetails to simulate an authenticated user
        Users user = new Users();
        user.setEmail("test@example.com");
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication mockedAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Configure the mock AuthenticationManager to return the simulated authentication
        when(authenticationManager.authenticate(any())).thenReturn(mockedAuthentication);

        // Execution
        // Call the attemptAuthentication method of the filter
        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        // Verification
        // Check that the result is not null and is equal to the simulated authentication
        assertNotNull(result);
        assertEquals(mockedAuthentication, result);
        // Verify that the authenticate method of the AuthenticationManager was called
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    /**
     * Test the login process with invalid credentials
     * Verifies that an AuthenticationException is thrown when the credentials are incorrect
     */
    @Test
    void attemptAuthentication_InvalidCredentials_ThrowsAuthenticationException() throws IOException {
        // Preparation
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Create credentials with an incorrect password
        AuthCredentials credentials = new AuthCredentials();
        credentials.setEmail("test@example.com");
        credentials.setPassword("wrongpassword");

        String jsonCredentials = new ObjectMapper().writeValueAsString(credentials);
        request.setContent(jsonCredentials.getBytes());

        // Configure the mock to throw an exception when authenticating
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationException("Invalid credentials") {});

        // Execution and Verification
        // Check that an AuthenticationException is thrown when attempting to authenticate
        assertThrows(AuthenticationException.class, () -> {
            jwtAuthenticationFilter.attemptAuthentication(request, response);
        });
    }

    /**
     * Test the authentication process with invalid JSON
     * Verifies that an attempt is made to authenticate with null credentials and an exception is thrown
     */
    @Test
    void attemptAuthentication_InvalidJson_ReturnsUnauthorizedResponse() throws IOException {
        // Preparation
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        // Set invalid JSON as request content
        request.setContent("invalid json".getBytes());

        // Execution
        jwtAuthenticationFilter.attemptAuthentication(request, response);

        // Verification: expect the response to be SC_UNAUTHORIZED
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        // Verify that the response contains the expected content
        String expectedErrorMessage = "Error al leer los datos de inicio de sesión";
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains(expectedErrorMessage));
    }
}
