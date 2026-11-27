package com.mjpascual.gestbanda.entities;


import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "voices")
public class Voices {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_at")
    private Date created_at;

    public static Voices from(VoicesResponse voice) {
        return Voices.builder()
                .id(voice.getId())
                .name(voice.getName())
                .updated_at(voice.getUpdated_at())
                .created_at(voice.getCreated_at())
                .build();

    }

}
