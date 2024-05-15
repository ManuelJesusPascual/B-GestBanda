package com.mjpascual.gestbanda.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "band_has_clothes")
public class BandHasClothes {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id")
    private Bands band_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothes_id")
    private Clothes clothes_id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "updated_at")
    private String updated_at;

    @Column(name = "created_at")
    private String created_at;
}
