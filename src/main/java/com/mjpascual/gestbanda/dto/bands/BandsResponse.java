package com.mjpascual.gestbanda.dto.bands;

import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.entities.Users;
import lombok.*;

import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BandsResponse {


    private UUID id;

    private String name;

    private String img;

    private String subscription_id;

    private Date subscription_end;


    public static BandsResponse from(Bands band) {
        return BandsResponse.builder()
                .id(band.getId())
                .name(band.getName())
                .img(band.getImg())
                .subscription_id(band.getSubscription_id())
                .subscription_end(band.getSubscription_end())

                .build();

    }

    public static BandsResponse from(Bands band, Users user) {
        return BandsResponse.builder()
                .id(band.getId())
                .name(band.getName())
                .img(band.getImg())
                .subscription_id(band.getSubscription_id())
                .subscription_end(band.getSubscription_end())
                .build();

    }

}
