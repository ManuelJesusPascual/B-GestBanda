package com.mjpascual.gestbanda.dto.bands;

import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.interfaces.IValidator;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.isNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateBandRequest implements IValidator {

    private String name;
    private UsersResponse user;

    /**
     * Verifies that required fields are not null. Throws a GestBandaException if
     * any required field is missing.
     */
    @Override
    public void verifyNulls() {
        if (isNull(name)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado un nombre", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (isNull(user)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado un usuario", ErrorCodes.ARGUMENT_NOT_VALID);
        }
    }

    /**
     * Trims any leading or trailing whitespace from string fields.
     */
    @Override
    public void trimFields() {
        name = name.trim();
    }

    /**
     * Validates that fields meet specific criteria. Ensures 'name' only contains letters and
     * spaces, and does not exceed 120 characters. Throws a GestBandaException for invalid fields.
     */
    @Override
    public void validateFields() {
        String bandNameRegex = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ. ]+$";

        if (!name.matches(bandNameRegex)) {
            throw new GestBandaException(new Exception(), "El nombre introducido no es válido", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if(name.length() > 120) {
            throw new GestBandaException(new Exception(), "El nombre introducido es demasiado largo", ErrorCodes.ARGUMENT_NOT_VALID);
        }
    }

    /**
     * Runs all validation steps in sequence: verifies non-null fields, trims whitespace,
     * and checks that fields conform to validation rules.
     */
    @Override
    public void validate() {
        verifyNulls();
        trimFields();
        validateFields();
    }
}
