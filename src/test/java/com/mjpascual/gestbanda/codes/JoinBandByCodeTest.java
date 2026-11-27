package com.mjpascual.gestbanda.codes;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.codes.JoinBandRequest;
import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.entities.Codes;
import com.mjpascual.gestbanda.entities.UsersBand;
import com.mjpascual.gestbanda.entities.Voices;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.CodesRepository;
import com.mjpascual.gestbanda.services.CodesService;
import com.mjpascual.gestbanda.services.UsersBandService;
import com.mjpascual.gestbanda.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for validating the behavior of CodesService methods related to
 * joining a band using a code. This class uses Mockito to mock dependencies
 * and to test various scenarios including valid, expired, and invalid codes.
 */
class JoinBandByCodeTest {

    @Mock
    private CodesRepository codesRepository;

    @Mock
    private UsersBandService usersBandService;

    @Mock
    private UsersService usersService;

    @InjectMocks
    private CodesService codesService;

    private static final String VALID_CODE = "VALID123";
    private static final String EXPIRED_CODE = "EXPIRED123";
    private static final String INVALID_CODE = "INVALID123";

    @BeforeEach
    void setUp() {
        // Initialize mocks and set up the required scenarios for the tests.
        MockitoAnnotations.openMocks(this);
        setupMocks();
    }

    /**
     * Sets up mock responses for different code scenarios.
     * This includes valid codes, expired codes, and invalid codes.
     */
    private void setupMocks() {
        // Setup for valid code
        Codes validCode = new Codes();
        validCode.setCode(VALID_CODE);
        validCode.setEndDate(new Date(System.currentTimeMillis() + 86400000)); // Tomorrow
        Bands band = new Bands();
        band.setId(UUID.randomUUID());
        band.setName("Test Band");
        validCode.setBand_id(band);
        Voices voice = new Voices();
        voice.setId(UUID.randomUUID());
        voice.setName("Sin Voz");
        validCode.setVoice_id(voice);
        when(codesRepository.findByCode(VALID_CODE)).thenReturn(validCode);

        // Setup for expired code
        Codes expiredCode = new Codes();
        expiredCode.setCode(EXPIRED_CODE);
        expiredCode.setEndDate(new Date(System.currentTimeMillis() - 86400000)); // Yesterday
        expiredCode.setBand_id(band);
        when(codesRepository.findByCode(EXPIRED_CODE)).thenReturn(expiredCode);

        // Setup for invalid code
        when(codesRepository.findByCode(INVALID_CODE)).thenReturn(null);
    }

    /**
     * Test to verify that a valid code returns the corresponding code.
     * It asserts that the response is not null and that the repository
     * was called with the valid code.
     */
    @Test
    void getCode_WithValidCode_ShouldReturnBand() {
        // Act
        Codes response = codesService.getCode(VALID_CODE);

        // Assert
        assertNotNull(response);
        verify(codesRepository).findByCode(VALID_CODE);
    }

    /**
     * Test to verify that an expired code throws a GestBandaException
     * with the appropriate error code.
     */
    @Test
    void getCode_WithExpiredCode_ShouldThrowException() {
        try {
            codesService.getCode(EXPIRED_CODE);
            fail("Expected GestBandaException to be thrown");
        } catch (GestBandaException e) {
            assertEquals(ErrorCodes.CODE_EXPIRED, e.getCode());
            verify(codesRepository).findByCode(EXPIRED_CODE);
        }
    }

    /**
     * Test to verify that an invalid code throws a GestBandaException
     * with the appropriate error code.
     */
    @Test
    void getCode_WithInvalidCode_ShouldThrowException() {
        try {
            codesService.getCode(INVALID_CODE);
            fail("Expected GestBandaException to be thrown");
        } catch (GestBandaException e) {
            assertEquals(ErrorCodes.CODE_NOT_FOUND, e.getCode());
            verify(codesRepository).findByCode(INVALID_CODE);
        }
    }

    /**
     * Test to verify that a valid code and user allows successful joining
     * of a band. It checks that the response is not null and that the
     * required methods are called on the mocked services.
     */
    @Test
    void joinBandByCode_WithValidCodeAndUser_ShouldSucceed() {
        // Arrange
        UsersResponse user = UsersResponse.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@test.com")
                .phone("123456789")
                .build();

        JoinBandRequest request = new JoinBandRequest();
        request.setCode(VALID_CODE);
        request.setUser(user);

        when(usersService.checkUser(any(UsersResponse.class))).thenReturn(user);
        doNothing().when(usersBandService).insertUserBand(any(BandsResponse.class), any(VoicesResponse.class), any(UsersResponse.class));

        // Act
        BandsResponse response = codesService.joinBandByCode(request);

        // Assert
        assertNotNull(response);
        verify(codesRepository).findByCode(VALID_CODE);
        verify(usersService).checkUser(any(UsersResponse.class));
        verify(usersBandService).insertUserBand(any(BandsResponse.class), any(VoicesResponse.class), any(UsersResponse.class));
    }

    /**
     * Test to verify that providing an invalid user throws a GestBandaException
     * with the appropriate error code.
     */
    @Test
    void joinBandByCode_WithInvalidUser_ShouldThrowException() {
        // Arrange
        UsersResponse invalidUser = UsersResponse.builder()
                .id(UUID.randomUUID())
                .build();

        when(usersService.checkUser(any(UsersResponse.class)))
                .thenThrow(new GestBandaException(new Exception(), "Usuario no válido", ErrorCodes.ARGUMENT_NOT_VALID));

        JoinBandRequest request = new JoinBandRequest();
        request.setCode(VALID_CODE);
        request.setUser(invalidUser);

        try {
            codesService.joinBandByCode(request);
            fail("Expected GestBandaException to be thrown");
        } catch (GestBandaException e) {
            assertEquals(ErrorCodes.ARGUMENT_NOT_VALID, e.getCode());
            verify(usersService).checkUser(any(UsersResponse.class));
        }
    }

    /**
     * Test to verify that attempting to join a band when the user is already
     * in that band throws a GestBandaException with the appropriate error code.
     */
    @Test
    void joinBandByCode_WhenUserAlreadyInBand_ShouldThrowException() {
        // Arrange
        UsersResponse user = UsersResponse.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@test.com")
                .phone("123456789")
                .build();

        JoinBandRequest request = new JoinBandRequest();
        request.setCode(VALID_CODE);
        request.setUser(user);

        when(usersService.checkUser(any(UsersResponse.class))).thenReturn(user);
        doThrow(new GestBandaException(new Exception(), "Usuario ya en la banda", ErrorCodes.USER_ALREADY_IN_BAND))
                .when(usersBandService).insertUserBand(any(BandsResponse.class), any(VoicesResponse.class), any(UsersResponse.class));

        try {
            codesService.joinBandByCode(request);
            fail("Expected GestBandaException to be thrown");
        } catch (GestBandaException e) {
            assertEquals(ErrorCodes.USER_ALREADY_IN_BAND, e.getCode());
            verify(usersBandService).insertUserBand(any(BandsResponse.class), any(VoicesResponse.class), any(UsersResponse.class));
        }
    }
}
