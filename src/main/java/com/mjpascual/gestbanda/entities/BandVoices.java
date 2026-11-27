package com.mjpascual.gestbanda.entities;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.usersBand.CreateUsersBandRequest;
import com.mjpascual.gestbanda.dto.usersBand.UsersBandResponse;
import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "band_voices")
public class BandVoices {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_band")
    private Bands id_band;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_voice")
    private Voices id_voice;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_at")
    private Date created_at;


    public static BandVoices from(VoicesResponse voice, BandsResponse band) {
        return BandVoices.builder()
                .id_voice(Voices.from(voice))
                .id_band(Bands.from(band))
                .created_at(new Date())
                .build();
    }
}
