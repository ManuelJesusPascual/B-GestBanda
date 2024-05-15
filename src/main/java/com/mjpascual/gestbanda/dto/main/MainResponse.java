package com.mjpascual.gestbanda.dto.main;


import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MainResponse {
    private BandsResponse band;
    private UsersResponse user;

    /**
     * TODO: Se que seguramente me diga que en vez de hacerlo así meta BandsDTO dentro de UsersDTO
     */
}
