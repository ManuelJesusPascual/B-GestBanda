package com.mjpascual.gestbanda.dto.codes;

import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.interfaces.IValidator;
import lombok.*;

import java.util.Date;
import java.util.UUID;

import static java.util.Objects.isNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateCodeRequest implements IValidator {

    private UUID voice_id;
    private UUID band_id;
    private Date end_date;

    /**
     * Method that checks that the required values are not null
     */
    @Override
    public void verifyNulls() {
        if (isNull(end_date)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado una fecha de fin", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (isNull(band_id)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado una banda", ErrorCodes.ARGUMENT_NOT_VALID);
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
