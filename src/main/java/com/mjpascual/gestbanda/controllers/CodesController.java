package com.mjpascual.gestbanda.controllers;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.codes.JoinBandRequest;
import com.mjpascual.gestbanda.services.BandsService;
import com.mjpascual.gestbanda.services.CodesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CodesController is a REST controller that handles requests related to band codes.
 * It provides an endpoint for users to join a band by submitting a valid code.
 */
@RestController
@RequestMapping("/api/codes")
public class CodesController {

    private final CodesService codesService;

    /**
     * Constructor for CodesController.
     * Initializes the controller with the required CodesService dependency.
     *
     * @param codesService the service responsible for handling band codes
     */
    public CodesController(CodesService codesService) {
        this.codesService = codesService;
    }

    /**
     * Handles POST requests to join a band by code.
     * Validates the JoinBandRequest, invokes the joinBandByCode method in CodesService,
     * and returns the corresponding BandsResponse.
     *
     * @param request the request body containing the band join code
     * @return a ResponseEntity containing the BandsResponse if successful, with HTTP status 200 (OK)
     */
    @PostMapping(value = "/joinBandByCode")
    public ResponseEntity<BandsResponse> getBandByCode(@RequestBody JoinBandRequest request) {
        request.validate();
        BandsResponse band = codesService.joinBandByCode(request);
        return new ResponseEntity<>(band, HttpStatus.OK);
    }
}
