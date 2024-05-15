package com.mjpascual.gestbanda.entities;

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
    private List<UsersInBand> usersInBand;

    @Transient
    private List<UserHasRoleInBand> userHasRoleInBand;

    @OneToMany(mappedBy = "band_id", fetch = FetchType.LAZY)
    private List<Codes> codes;

    @Transient
    private List<BandHasClothes> bandHasClothes;

    @Transient
    private List<BandHasInstruments> bandHasInstruments;

}
