package com.mjpascual.gestbanda.dto.usersBand;


import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;

import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import com.mjpascual.gestbanda.entities.BandVoices;
import com.mjpascual.gestbanda.entities.UsersBand;
import com.mjpascual.gestbanda.entities.Voices;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BandsVoicesResponse {

    private BandsResponse band;

    private VoicesResponse voice;

    public static BandsVoicesResponse from(BandVoices response) {
        return BandsVoicesResponse.builder()
                .band(BandsResponse.from(response.getId_band()))
                .voice(VoicesResponse.from(response.getId_voice()))
                .build();
    }
}
