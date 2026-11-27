package com.mjpascual.gestbanda.controllers;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.codes.CodesResponse;
import com.mjpascual.gestbanda.dto.codes.CreateCodeRequest;
import com.mjpascual.gestbanda.dto.codes.JoinBandRequest;
import com.mjpascual.gestbanda.dto.codes.UpdateCodeRequest;
import com.mjpascual.gestbanda.services.BandsService;
import com.mjpascual.gestbanda.services.CodesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * CodesController is a REST controller that handles requests related to band codes.
 * It provides endpoints for joining a band, creating codes, retrieving codes, updating codes, and deleting codes.
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
    public ResponseEntity<BandsResponse> joinBandByCode(@RequestBody JoinBandRequest request) {
        request.validate();
        BandsResponse band = codesService.joinBandByCode(request);
        return new ResponseEntity<>(band, HttpStatus.OK);
    }

    /**
     * Handles POST requests to create a new band code.
     * Validates the CreateCodeRequest, invokes the createBandCode method in CodesService,
     * and returns the created CodesResponse.
     *
     * @param request the request body containing details for the new band code
     * @return a ResponseEntity containing the created CodesResponse, with HTTP status 201 (Created)
     */
    @PostMapping(value = "/createBandCode")
    public ResponseEntity<CodesResponse> createBandCode(@RequestBody CreateCodeRequest request) {
        request.validate();
        CodesResponse code = codesService.createBandCode(request);
        return new ResponseEntity<>(code, HttpStatus.CREATED);
    }

    /**
     * Handles GET requests to retrieve band codes by band ID.
     * Invokes the getBandCodes method in CodesService and returns a list of CodesResponse.
     *
     * @param bandID the UUID of the band whose codes are to be retrieved
     * @return a ResponseEntity containing a list of CodesResponse, with HTTP status 200 (OK)
     */
    @GetMapping(value = "/getBandCodes")
    public ResponseEntity<List<CodesResponse>> getBandCodes(@RequestParam("id") UUID bandID) {
        List<CodesResponse> res = codesService.getBandCodes(bandID);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Handles PUT requests to update an existing band code.
     * Validates the UpdateCodeRequest, invokes the updateBandCode method in CodesService,
     * and returns the updated CodesResponse.
     *
     * @param request the request body containing updated details for the band code
     * @return a ResponseEntity containing the updated CodesResponse, with HTTP status 200 (OK)
     */
    @PutMapping("/updateBandCode")
    public ResponseEntity<CodesResponse> updateBandCode(@RequestBody UpdateCodeRequest request) {
        request.validate();
        CodesResponse code = codesService.updateBandCode(request);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    /**
     * Handles DELETE requests to delete a band code by its ID.
     * Invokes the deleteBandCode method in CodesService.
     *
     * @param idCode the UUID of the band code to be deleted
     */
    @DeleteMapping(value = "/deleteBandCode")
    public void deleteBandCode(@RequestParam("idCode") UUID idCode) {
        codesService.deleteBandCode(idCode);
    }
}
