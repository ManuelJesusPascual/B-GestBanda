package com.mjpascual.gestbanda.dto.voices;

import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.interfaces.IValidator;
import lombok.*;

import java.util.UUID;

import static java.util.Objects.isNull;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AddVoiceToBandRequest implements IValidator {

    String voice_name;
    UUID band_uuid;

    @Override
    public void verifyNulls() {

        if (isNull(voice_name) || voice_name.length()<= 0) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado una voz", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (isNull(band_uuid)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado una banda", ErrorCodes.ARGUMENT_NOT_VALID);
        }

    }

    @Override
    public void trimFields() {
        voice_name = voice_name.trim();
    }

    @Override
    public void validate() {
        verifyNulls();
        trimFields();
    }

}
