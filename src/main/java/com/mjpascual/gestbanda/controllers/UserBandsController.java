package com.mjpascual.gestbanda.controllers;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.bands.CreateBandRequest;
import com.mjpascual.gestbanda.dto.usersBand.UsersBandResponse;
import com.mjpascual.gestbanda.services.BandsService;
import com.mjpascual.gestbanda.services.UsersBandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Controller for handling user-band related operations.
 */
@RestController
@RequestMapping("/api/userBands")
public class UserBandsController {

    private final UsersBandService usersBandService;

    public UserBandsController(UsersBandService usersBandService) {
        this.usersBandService = usersBandService;
    }

    /**
     * Retrieves the list of bands associated with a specific user.
     *
     * @param id User ID for whom bands are to be retrieved.
     * @return ResponseEntity containing a list of UserBandResponse.
     */
    @GetMapping(value = "/getUserBands")
    public ResponseEntity<List<UsersBandResponse>> registerBand(@RequestParam("id") UUID id) {
        List<UsersBandResponse> res = usersBandService.findByUser(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
