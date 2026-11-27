package com.mjpascual.gestbanda.dto.users;

import com.mjpascual.gestbanda.dto.usersBand.UsersBandResponse;
import com.mjpascual.gestbanda.entities.Users;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UsersResponse {

    private UUID id;

    private String name;

    private String email;

    private String phone;


    public static UsersResponse from(Users user) {
        return UsersResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();

    }

}
