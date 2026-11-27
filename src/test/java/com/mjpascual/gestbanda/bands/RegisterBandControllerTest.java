package com.mjpascual.gestbanda.bands;

import com.mjpascual.gestbanda.controllers.BandController;
import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.bands.CreateBandRequest;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.services.BandsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the BandController class.
 * This class covers various scenarios for the registerBand method,
 * validating successful registration as well as failure cases due to invalid inputs.
 */
class RegisterBandControllerTest {

    @Mock
    private BandsService bandsService;

    @InjectMocks
    private BandController bandController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for a successful band registration.
     * Verifies that a valid band name and user in the request, along with an image,
     * result in a successful registration.
     */
    @Test
    @DisplayName("Registro exitoso de banda")
    void registerBand_Success() {
        CreateBandRequest createBandRequest = new CreateBandRequest("Banda Ejemplo", new UsersResponse());
        MultipartFile img = mock(MultipartFile.class);
        BandsResponse bandsResponse = new BandsResponse();

        when(bandsService.createBand(any(CreateBandRequest.class), any(MultipartFile.class))).thenReturn(bandsResponse);

        ResponseEntity<BandsResponse> response = bandController.registerBand(createBandRequest, img);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bandsResponse, response.getBody());
        verify(bandsService, times(1)).createBand(createBandRequest, img);
    }

    /**
     * Test to check failure in band registration due to invalid band name.
     * Validates that an invalid name in the request triggers a GestBandaException.
     */
    @Test
    @DisplayName("Fallo al registrar banda - Nombre inválido")
    void registerBand_InvalidName_Failure() {
        CreateBandRequest createBandRequest = new CreateBandRequest("Nombre123", new UsersResponse());
        MultipartFile img = mock(MultipartFile.class);

        assertThrows(GestBandaException.class, () -> bandController.registerBand(createBandRequest, img));
        verify(bandsService, never()).createBand(any(CreateBandRequest.class), any(MultipartFile.class));
    }

    /**
     * Test to check failure due to band name exceeding allowed length.
     * Confirms that if the band name is longer than 120 characters, an exception is thrown.
     */
    @Test
    @DisplayName("Fallo al registrar banda - Nombre demasiado largo")
    void registerBand_NameTooLong_Failure() {
        String longName = "a".repeat(121);
        CreateBandRequest createBandRequest = new CreateBandRequest(longName, new UsersResponse());
        MultipartFile img = mock(MultipartFile.class);

        assertThrows(GestBandaException.class, () -> bandController.registerBand(createBandRequest, img));
        verify(bandsService, never()).createBand(any(CreateBandRequest.class), any(MultipartFile.class));
    }

    /**
     * Test to validate failure due to null user in the request.
     * Ensures that a GestBandaException is thrown when the user field is null.
     */
    @Test
    @DisplayName("Registro de banda sin usuario en el body (se toma del JWT)")
    void registerBand_WithoutBodyUser_Success() {
        CreateBandRequest createBandRequest = new CreateBandRequest("Nombre Valido", null);
        MultipartFile img = mock(MultipartFile.class);
        when(bandsService.createBand(any(CreateBandRequest.class), any(MultipartFile.class))).thenReturn(new BandsResponse());

        ResponseEntity<BandsResponse> response = bandController.registerBand(createBandRequest, img);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(bandsService, times(1)).createBand(createBandRequest, img);
    }

    /**
     * Test to simulate an error during image upload.
     * Verifies that a GestBandaException is thrown if the image upload process fails.
     */
    @Test
    @DisplayName("Fallo al registrar banda - Error en la carga de imagen")
    void registerBand_ImageUploadError_Failure() {
        CreateBandRequest createBandRequest = new CreateBandRequest("Nombre Valido", new UsersResponse());
        MultipartFile img = mock(MultipartFile.class);

        when(bandsService.createBand(any(CreateBandRequest.class), any(MultipartFile.class)))
                .thenThrow(new GestBandaException(new Exception(), "Error al intentar guardar la imagen", ErrorCodes.UPLOAD_IMAGE_ERROR));

        assertThrows(GestBandaException.class, () -> bandController.registerBand(createBandRequest, img));
        verify(bandsService, times(1)).createBand(any(CreateBandRequest.class), any(MultipartFile.class));
    }

    /**
     * Test to confirm HTTP status 201 is returned upon successful registration.
     * Checks that a successful registration returns the correct HTTP response status and includes a valid response body.
     */
    @Test
    @DisplayName("Respuesta HTTP 201 Created al registrar exitosamente")
    void registerBand_HttpStatusCreatedOnSuccess() {
        CreateBandRequest createBandRequest = new CreateBandRequest("Banda Ejemplo", new UsersResponse());
        MultipartFile img = mock(MultipartFile.class);
        BandsResponse bandsResponse = new BandsResponse();

        when(bandsService.createBand(any(CreateBandRequest.class), any(MultipartFile.class))).thenReturn(bandsResponse);

        ResponseEntity<BandsResponse> response = bandController.registerBand(createBandRequest, img);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bandsService, times(1)).createBand(createBandRequest, img);
    }
}
