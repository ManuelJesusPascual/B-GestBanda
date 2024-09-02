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
 * Controlador de la entidad Users
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * Endpoint que se encargará de llamar a la validación del DTO para veríficar que el usuario que nos ha llegado
     * es válido y posteriormente llamará al servicio para insertar el usuario en la BBDD.
     *
     * @param createUserRequest Usuario a registrar.
     * @return Usuario insertado en la BBDD
     */
    @PostMapping("/registerUser")
    public ResponseEntity<UsersResponse> registerUser(@RequestBody CreateUserRequest createUserRequest) {
        UsersResponse response;

        createUserRequest.validate();

        response = usersService.createUser(createUserRequest);

        /**
         * Insertar token en los headers
         */
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + createToken(response.getEmail()));


        return new ResponseEntity<>(response,headers, HttpStatus.CREATED);
    }
}
