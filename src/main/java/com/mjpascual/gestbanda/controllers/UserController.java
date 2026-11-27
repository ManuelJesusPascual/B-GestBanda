package com.mjpascual.gestbanda.controllers;

import com.mjpascual.gestbanda.config.Security.TokenUtils;
import com.mjpascual.gestbanda.dto.users.CreateUserRequest;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.services.UsersService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


/**
 * Controller for the Users entity
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UsersService usersService;
    private final TokenUtils tokenUtils;

    public UserController(UsersService usersService, TokenUtils tokenUtils) {
        this.usersService = usersService;
        this.tokenUtils = tokenUtils;
    }

    /**
     * Endpoint responsible for validating the DTO to verify that the received user is valid,
     * and then calling the service to insert the user into the database.
     *
     * @param createUserRequest User to be registered.
     * @return The user inserted into the database.
     */
    @PostMapping("/registerUser")
    public ResponseEntity<UsersResponse> registerUser(@RequestBody CreateUserRequest createUserRequest) {
        UsersResponse response;

        createUserRequest.validate();

        response = usersService.createUser(createUserRequest);

        /**
         * Insert token into headers
         */
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenUtils.createToken(response.getEmail()));

        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }

    /**
     * Retrieves a user by their unique identifier (UUID).
     *
     * This method processes a GET request to the "/getUserByID" endpoint.
     * It takes a UUID as a request parameter and uses the usersService
     * to find and return the corresponding user data encapsulated in a
     * UsersResponse object.
     *
     * @param id The unique identifier of the user to be retrieved.
     * @return A ResponseEntity containing the UsersResponse object and
     *         an HTTP status of OK (200) if the user is found.
     */
    @GetMapping("/getUserByID")
    public ResponseEntity<UsersResponse> getUserById(@RequestParam("id") UUID id) {
        UsersResponse response;
        response = usersService.findUser(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
