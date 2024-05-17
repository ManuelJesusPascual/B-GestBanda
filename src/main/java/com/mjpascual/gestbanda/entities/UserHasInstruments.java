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
@Table(name = "user_has_instruments")
public class UserHasInstruments {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id")
    private Instruments instrument_id;

    @Column(name = "owner")
    private Boolean owner;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "updated_at")
    private String updated_at;

    @Column(name = "created_at")
    private String created_at;

}
