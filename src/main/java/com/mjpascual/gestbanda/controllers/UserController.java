package com.mjpascual.gestbanda.controllers;

import com.mjpascual.gestbanda.dto.users.CreateUserRequest;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.services.UsersService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mjpascual.gestbanda.config.Security.TokenUtils.createToken;

/**
 * Controller for the Users entity
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
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
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + createToken(response.getEmail()));

        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }
}
