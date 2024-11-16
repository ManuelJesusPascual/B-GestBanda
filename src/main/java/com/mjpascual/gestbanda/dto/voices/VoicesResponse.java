package com.mjpascual.gestbanda.dto.voices;

import com.mjpascual.gestbanda.entities.BandVoices;
import com.mjpascual.gestbanda.entities.Roles;
import com.mjpascual.gestbanda.entities.Voices;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VoicesResponse {

    private UUID id;
    private String name;
    private Date updated_at;
    private Date created_at;

    public static VoicesResponse from(Voices voz) {
        return VoicesResponse.builder()
                .id(voz.getId())
                .name(voz.getName())
                .updated_at(voz.getUpdated_at())
                .created_at(voz.getCreated_at())
                .build();

    }


    public static List<VoicesResponse> fromBandVoices(List<BandVoices> voices) {
        List<VoicesResponse> voicesResponses = new ArrayList<>();
        for(BandVoices voz : voices) {
            voicesResponses.add(VoicesResponse.from(voz.getId_voice()));
        }
        return voicesResponses;
    }

    public static List<VoicesResponse> fromVoices(List<Voices> voices) {
        List<VoicesResponse> voicesResponses = new ArrayList<>();
        for(Voices voz : voices) {
            voicesResponses.add(VoicesResponse.from(voz));
        }
        return voicesResponses;
    }
}


