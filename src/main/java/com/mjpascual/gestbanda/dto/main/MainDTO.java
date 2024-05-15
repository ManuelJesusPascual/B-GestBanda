package com.mjpascual.gestbanda.dto.main;


import com.mjpascual.gestbanda.dto.bands.BandsDTO;
import com.mjpascual.gestbanda.dto.users.UsersDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MainDTO {
    private BandsDTO band;
    private UsersDTO user;

    /**
     * TODO: Se que seguramente me diga que en vez de hacerlo así meta BandsDTO dentro de UsersDTO
     */
}
