package com.mjpascual.gestbanda.controllers;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.bands.CreateBandRequest;
import com.mjpascual.gestbanda.dto.codes.CodesResponse;
import com.mjpascual.gestbanda.dto.codes.UpdateCodeRequest;
import com.mjpascual.gestbanda.dto.usersBand.ModifyUsersBandRequest;
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
     * @param userId User ID for whom bands are to be retrieved.
     * @return ResponseEntity containing a list of UserBandResponse.
     */
    @GetMapping(value = "/getUserBands")
    public ResponseEntity<List<UsersBandResponse>> getUserBands(@RequestParam("id") UUID userId) {
        List<UsersBandResponse> res = usersBandService.findByUser(userId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves the list of users associated with a specific band.
     *
     * @param bandId Band ID for which users are to be retrieved.
     * @return ResponseEntity containing a list of UserBandResponse.
     */
    @GetMapping(value = "/getBandUsers")
    public ResponseEntity<List<UsersBandResponse>> getBandUsers(@RequestParam("id") UUID bandId) {
        List<UsersBandResponse> res = usersBandService.findByBand(bandId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Modifies the association of a user with a band.
     *
     * @param request Request object containing the modification details.
     */
    @PutMapping("/modifyUserBand")
    public void modifyUserBand(@RequestBody ModifyUsersBandRequest request) {
        request.validate();
        usersBandService.modifyUserBand(request);
    }

    /**
     * Deletes the association of a user with a band.
     *
     * @param idUser User ID to be deleted from the band.
     * @param idBand Band ID from which the user is to be deleted.
     */
    @DeleteMapping(value = "/deleteUserBand")
    public void deleteBandCode(@RequestParam("idUser") UUID idUser, @RequestParam("idBand") UUID idBand) {
        usersBandService.deleteUserBand(idUser,idBand);
    }

}
