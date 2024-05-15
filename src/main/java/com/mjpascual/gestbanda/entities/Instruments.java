package com.mjpascual.gestbanda.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "instruments")
public class Instruments {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "img")
    private String img;

    @Column(name = "updated_at")
    private String updated_at;

    @Column(name = "created_at")
    private String created_at;

    @Transient
    private List<BandHasInstruments> bandHasInstruments;

    @Transient
    private List<UserHasInstruments> userHasInstruments;

}
