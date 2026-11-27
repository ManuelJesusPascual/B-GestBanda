package com.mjpascual.gestbanda.entities;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.bands.CreateBandRequest;
import com.mjpascual.gestbanda.dto.img.ImgResponse;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "bands")
public class Bands {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "img")
    private String img;

    @Column(name = "subscription_id")
    private String subscription_id;

    @Column(name = "subscription_end")
    private Date subscription_end;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_at")
    private Date created_at;

    @Transient
    private List<UsersBand> usersBand;

    @OneToMany(mappedBy = "band_id", fetch = FetchType.LAZY)
    private List<Codes> codes;

    public static Bands from(CreateBandRequest createBandRequest) {
        return Bands.builder()
                .name(createBandRequest.getName())
                .build();

    }

    public static Bands from(BandsResponse bandDTO) {
        return Bands.builder()
                .id(bandDTO.getId())
                .name(bandDTO.getName())
                .img(bandDTO.getImg())
                .subscription_id(bandDTO.getSubscription_id())
                .subscription_end(bandDTO.getSubscription_end())
                .build();

    }


    public static Bands from(CreateBandRequest band, ImgResponse imgResponse) {
        return Bands.builder()
                .name(band.getName())
                .img(imgResponse.getData().getLink())
                .subscription_id(UUID.randomUUID().toString())
                .subscription_end(new Date())
                .created_at(new Date())
                .build();
    }
}
