package com.mjpascual.gestbanda.dto.users;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateUserRequest {

    private String name;
    private String email;
    private String password;
    private String phone;

}
