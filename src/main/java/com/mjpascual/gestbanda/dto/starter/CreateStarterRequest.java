package com.mjpascual.gestbanda.dto.starter;

import com.mjpascual.gestbanda.dto.bands.CreateBandRequest;
import com.mjpascual.gestbanda.dto.users.CreateUserRequest;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateStarterRequest {

    private CreateUserRequest user;
    private CreateBandRequest band;

}
