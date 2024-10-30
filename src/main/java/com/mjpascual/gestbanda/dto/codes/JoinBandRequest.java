package com.mjpascual.gestbanda.dto.codes;

import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.interfaces.IValidator;
import lombok.*;

import static java.util.Objects.isNull;

/**
 * JoinBandRequest represents a request to join a band using a code.
 * This class validates the required fields, ensuring they are non-null and meet specific format requirements.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class JoinBandRequest implements IValidator {

    private String code;
    private UsersResponse user;

    /**
     * Checks for null values in required fields. If a required field is null, it throws a GestBandaException.
     * This method is part of the validation process.
     *
     * @throws GestBandaException if the code or user is null
     */
    @Override
    public void verifyNulls() {
        if (isNull(code)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado un código", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (isNull(user)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado un usuario", ErrorCodes.ARGUMENT_NOT_VALID);
        }
    }

    /**
     * Trims whitespace from the code field to ensure uniformity.
     * This method is part of the validation process.
     */
    @Override
    public void trimFields() {
        code = code.trim();
    }

    /**
     * Validates the format and length of the code field.
     * The code must be a 6-digit number; otherwise, it throws a GestBandaException.
     *
     * @throws GestBandaException if the code does not match the required format or length
     */
    @Override
    public void validateFields() {
        String codeRegex = "^[0-9]+$";

        if (!code.matches(codeRegex)) {
            throw new GestBandaException(new Exception(), "El codigo introducido no es valido", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (code.length() != 6) {
            throw new GestBandaException(new Exception(), "El codigo introducido no es valido", ErrorCodes.ARGUMENT_NOT_VALID);
        }
    }

    /**
     * Validates the JoinBandRequest by checking for nulls, trimming fields, and validating field formats.
     * This is the main validation method that runs all necessary checks.
     */
    @Override
    public void validate() {
        verifyNulls();
        trimFields();
        validateFields();
    }
}
