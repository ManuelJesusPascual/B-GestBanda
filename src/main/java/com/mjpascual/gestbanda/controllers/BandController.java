package com.mjpascual.gestbanda.controllers;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.bands.CreateBandRequest;
import com.mjpascual.gestbanda.services.BandsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for managing Band-related operations.
 */
@RestController
@RequestMapping("/api/band")
public class BandController {

    private BandsService bandsService;

    public BandController(BandsService bandsService) {
        this.bandsService = bandsService;
    }

    /**
     * Endpoint for registering a new band. Validates the incoming data and passes it to
     * the service layer to persist the band information and image.
     *
     * @param createBandRequest Contains data required to create a band.
     * @param img Image file representing the band's logo or photo.
     * @return ResponseEntity containing the registered band's details and HTTP status.
     */
    @PostMapping("/registerBand")
    public ResponseEntity<BandsResponse> registerBand(@RequestPart("createBandRequest") CreateBandRequest createBandRequest,
                                                      @RequestParam("img") MultipartFile img) {
        BandsResponse response;

        createBandRequest.validate();

        response = bandsService.createBand(createBandRequest, img);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
