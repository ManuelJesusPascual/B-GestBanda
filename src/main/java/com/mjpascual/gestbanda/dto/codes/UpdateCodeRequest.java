package com.mjpascual.gestbanda.dto.codes;


import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import com.mjpascual.gestbanda.entities.Codes;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.interfaces.IValidator;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UpdateCodeRequest implements IValidator {

    private UUID id;
    private UUID band_id;
    private UUID voice_id;
    private Date end_date;


    /**
     * Method that checks that the required values are not null
     */
    @Override
    public void verifyNulls() {
        if (isNull(id)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado una ID de código", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (isNull(band_id)) {
            throw new GestBandaException(new Exception(),"No se ha proporcionado un ID de banda", ErrorCodes.ARGUMENT_NOT_VALID);
        }

        if (isNull(end_date)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado una fecha de fin", ErrorCodes.ARGUMENT_NOT_VALID);
        }


    }

    /**
     * Method responsible for calling the various validations.
     */
    @Override
    public void validate() {
        verifyNulls();
    }

}


