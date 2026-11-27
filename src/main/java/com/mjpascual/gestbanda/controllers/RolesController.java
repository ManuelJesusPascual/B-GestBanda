package com.mjpascual.gestbanda.controllers;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.bands.CreateBandRequest;
import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.services.BandsService;
import com.mjpascual.gestbanda.services.RolesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller for managing Band-related operations.
 */
@RestController
@RequestMapping("/api/roles")
public class RolesController {

    private RolesService rolesService;

    /**
     * Constructor for RolesController.
     *
     * @param rolesService the service used to manage roles
     */
    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    /**
     * Endpoint to retrieve all roles.
     *
     * @return ResponseEntity containing a list of RolesResponse and HTTP status
     */
    @GetMapping(value = "/getAllRoles")
    public ResponseEntity<List<RolesResponse>> getAllRoles() {
        List<RolesResponse> response;

        response = rolesService.getAllRoles();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
