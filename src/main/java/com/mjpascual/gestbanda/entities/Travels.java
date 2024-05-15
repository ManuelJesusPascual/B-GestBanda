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
@Table(name = "travels")
public class Travels {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "img")
    private String img;

    @Column(name = "hourhq")
    private Date hourhq;

    @Column(name = "hourplace")
    private Date hourplace;

    @Column(name="latitude")
    private Double latitude;

    @Column(name="longitude")
    private Double longitude;

    @Column(name="bus_offer")
    private Boolean bus_offer;

    @JoinColumn(name="band_id") @ManyToOne(fetch = FetchType.LAZY)
    private Bands band_id;

    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "created_at")
    private Date created_at;

    @Transient
    private List<UserGoTravel> userGoTravel;

}
