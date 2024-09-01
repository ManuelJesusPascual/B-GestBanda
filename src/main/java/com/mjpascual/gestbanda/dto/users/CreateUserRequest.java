package com.mjpascual.gestbanda.dto.users;


import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.interfaces.IValidator;
import lombok.*;

import static java.util.Objects.isNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateUserRequest implements IValidator {

    private String name;
    private String email;
    private String phone;
    private String password;

    /**
     * Método que comprobará que los valores obligatorios no son null
     */
    @Override
    public void verifyNulls() {
        if (isNull(name)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado un nombre", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (isNull(email)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado un email", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (isNull(phone)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado un teléfono", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (isNull(password)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado una contraseña", ErrorCodes.ARGUMENT_NOT_VALID);
        }
    }

    /**
     * Método que elimina los espacios al principio y al final de los distintos atributos.
     */
    @Override
    public void trimFields() {
        name = name.trim();
        email = email.trim();
        phone = phone.trim();
    }

    /**
     * Método que valida los atributos comprobandolos contra un regex
     *
     * @return true si es válido, false si no es válido.
     */
    @Override
    public void validateFields() {
        String nameRegex = "^[a-zA-Z ]+$";
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String phoneRegex = "^[0-9]{9}$";
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$";
        if (!name.matches(nameRegex)) {
            throw new GestBandaException(new Exception(), "El nombre introducido no es válido", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (!email.matches(emailRegex)) {
            throw new GestBandaException(new Exception(), "El email introducido no es válido", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (!phone.matches(phoneRegex)) {
            throw new GestBandaException(new Exception(), "El telefono introducido no es válido", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (!password.matches(passwordRegex)) {
            throw new GestBandaException(new Exception(), "La contraseña introducida no es válido", ErrorCodes.ARGUMENT_NOT_VALID);
        }

    }

    /**
     * Método encargado de llamar a las distintas validaciones
     */
    @Override
    public void validate() {
        verifyNulls();
        trimFields();
        validateFields();
    }
}
