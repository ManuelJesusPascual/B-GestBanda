package com.mjpascual.gestbanda.entities;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.codes.CreateCodeRequest;
import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.security.SecureRandom;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "codes")
public class Codes {

    private static final SecureRandom RANDOM = new SecureRandom();
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "code")
    private String code;

    @Column(name = "end_date")
    private Date endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id")
    private Bands band_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voice_id")
    private Voices voice_id;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_at")
    private Date created_at;

    public static Codes from(CreateCodeRequest codeRequest, VoicesResponse voice, BandsResponse band) {
        return Codes.builder()
                .code(String.valueOf(RANDOM.nextInt(1000000)))
                .endDate(codeRequest.getEnd_date())
                .voice_id(voice != null ? Voices.from(voice) : null)
                .band_id(Bands.from(band))
                .created_at(new Date())
                .build();

    }

}
