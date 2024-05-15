package com.mjpascual.gestbanda.dto.bands;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BandsDTO {



    private UUID id;

    private String name;

    private String img;

    private String subscription_id;

    private Date subscription_end;

    private Date updated_at;

    private Date created_at;

}
