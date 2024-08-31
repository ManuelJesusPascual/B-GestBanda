package com.mjpascual.gestbanda.entities;

import com.mjpascual.gestbanda.dto.usersInBand.UsersInBandResponse;
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
@Table(name = "users_in_band")
public class UsersInBand {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id")
    private Bands band_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user_id;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_at")
    private Date created_at;


    public static UsersInBand from(UsersInBandResponse usersInBandResponse) {

        return UsersInBand.builder()
                .band_id(usersInBandResponse.getBand_id())
                .user_id(usersInBandResponse.getUser_id())
                .build();

    }

    public static List<UsersInBand> from(List<UsersInBandResponse> usersInBandResponse) {

        List<UsersInBand> usersInBandList = new ArrayList<>();
        for (UsersInBandResponse iterator : usersInBandResponse) {
            usersInBandList.add(UsersInBand.from(iterator));
        }

        return usersInBandList;

    }
}
