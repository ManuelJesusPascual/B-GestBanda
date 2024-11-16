package com.mjpascual.gestbanda.entities;

import com.mjpascual.gestbanda.dto.usersBand.CreateUsersBandRequest;
import com.mjpascual.gestbanda.dto.usersBand.UsersBandResponse;
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
@Table(name = "user_band")
public class UsersBand {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id")
    private Roles rol_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voice_id")
    private Voices voice_id;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_at")
    private Date created_at;


    public static UsersBand from(UsersBandResponse usersInBandResponse) {

        return UsersBand.builder()
                .band_id(Bands.from(usersInBandResponse.getBand()))
                .user_id(Users.from(usersInBandResponse.getUser()))
                .rol_id(Roles.from(usersInBandResponse.getRole()))
                .voice_id(usersInBandResponse.getVoice() != null ? Voices.from(usersInBandResponse.getVoice()): null)
                .build();

    }

    public static List<UsersBand> from(List<UsersBandResponse> usersInBandResponse) {

        List<UsersBand> usersBandList = new ArrayList<>();
        for (UsersBandResponse iterator : usersInBandResponse) {
            usersBandList.add(UsersBand.from(iterator));
        }

        return usersBandList;

    }

    public static UsersBand from(CreateUsersBandRequest request) {
        return UsersBand.builder()
                .band_id(Bands.from(request.getBand_id()))
                .user_id(Users.from(request.getUser_id()))
                .rol_id(Roles.from(request.getRole_id()))
                .voice_id(request.getVoice_id() != null ? Voices.from(request.getVoice_id()): null)
                .created_at(new Date())
                .build();
    }
}
