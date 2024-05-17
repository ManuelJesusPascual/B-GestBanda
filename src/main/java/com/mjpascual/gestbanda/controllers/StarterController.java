package com.mjpascual.gestbanda.controllers;

import com.mjpascual.gestbanda.dto.main.MainResponse;
import com.mjpascual.gestbanda.dto.starter.CreateStarterRequest;
import com.mjpascual.gestbanda.services.StarterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // TODO: @RestController o Controller?
public class StarterController {


    private StarterService starterService;

    @Autowired
    public StarterController(StarterService starterService) {
        this.starterService = starterService;
    }

    /**
     * POST que llamará al StarterService para crear una banda y un usuario.
     * @param request CreateStarterRequest que contendrá el usuario y la banda a crear
     * @return MainDTO
     */
    @PostMapping("/createStarter")
    public ResponseEntity<MainResponse> createUserBand(@RequestBody CreateStarterRequest request){

        MainResponse response = starterService.createBand(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
