package com.mjpascual.gestbanda.controllers;

import com.mjpascual.gestbanda.dto.usersBand.BandsVoicesResponse;
import com.mjpascual.gestbanda.dto.usersBand.UsersBandResponse;
import com.mjpascual.gestbanda.dto.voices.AddVoiceToBandRequest;
import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import com.mjpascual.gestbanda.services.BandVoicesService;
import com.mjpascual.gestbanda.services.VoicesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * BandVoicesController is responsible for handling HTTP requests related to band voices.
 * It provides endpoints to add, retrieve, and delete voices associated with a band.
 */
@RestController
@RequestMapping("/api/bandVoices")
public class BandVoicesController {

    private final BandVoicesService bandVoicesService;

    /**
     * Constructs a BandVoicesController with the specified BandVoicesService.
     *
     * @param bandVoicesService the service to handle band voices operations
     */
    public BandVoicesController(BandVoicesService bandVoicesService) {
        this.bandVoicesService = bandVoicesService;
    }

    /**
     * Adds a voice to a band based on the provided request.
     *
     * @param addVoiceToBandRequest the request containing the voice details to be added
     * @return a ResponseEntity containing the response data and the HTTP status
     */
    @PostMapping(value = "/addVoiceToBand")
    public ResponseEntity<VoicesResponse> createVoice(@RequestBody AddVoiceToBandRequest addVoiceToBandRequest) {
        VoicesResponse response;
        addVoiceToBandRequest.validate();

        response = bandVoicesService.addVoiceToBand(addVoiceToBandRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves the voices associated with a specific band.
     *
     * @param id the unique identifier of the band
     * @return a ResponseEntity containing the list of voices and the HTTP status
     */
    @GetMapping(value = "/getVoicesBand")
    public ResponseEntity<List<VoicesResponse>> getVoicesBand(@RequestParam("id") UUID id) {
        List<VoicesResponse> res = bandVoicesService.getVoicesBand(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves the voices that are not associated with a specific band.
     *
     * @param id the unique identifier of the band
     * @return a ResponseEntity containing the list of voices and the HTTP status
     */
    @GetMapping(value = "/getVoicesBandNotHave")
    public ResponseEntity<List<VoicesResponse>> getVoicesBandNotHave(@RequestParam("id") UUID id) {
        List<VoicesResponse> res = bandVoicesService.getVoicesBandNotHave(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Deletes a specific voice from a band.
     *
     * @param idVoice the unique identifier of the voice to be deleted
     * @param idBand the unique identifier of the band from which the voice is to be deleted
     */
    @DeleteMapping(value = "/deleteVoiceBand")
    public void deleteVoiceBand(@RequestParam("idVoice") UUID idVoice, @RequestParam("idBand") UUID idBand) {
        bandVoicesService.deleteVoiceBand(idVoice, idBand);
    }
}
