package com.mjpascual.gestbanda.dto.codes;


import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import com.mjpascual.gestbanda.entities.Codes;
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
public class CodesResponse {

    private UUID id;
    private String code;
    private VoicesResponse voice;
    private Date endDate;
    private Date createdDate;


    public static CodesResponse from(Codes code) {
        return CodesResponse.builder()
                .id(code.getId())
                .code(code.getCode())
                .voice(code.getVoice_id() != null ?VoicesResponse.from(code.getVoice_id()): null)
                .endDate(code.getEndDate())
                .createdDate(code.getCreated_at())
                .build();

    }


    public static List<CodesResponse> from(List<Codes> codesList) {
        List<CodesResponse> codesResponses = new ArrayList<>();
        for(Codes code : codesList) {
            codesResponses.add(CodesResponse.from(code));
        }
        return codesResponses;
    }
}


