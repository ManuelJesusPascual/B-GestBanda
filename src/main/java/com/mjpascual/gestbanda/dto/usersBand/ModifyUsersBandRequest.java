package com.mjpascual.gestbanda.dto.usersBand;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
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
public class ModifyUsersBandRequest implements IValidator {

    private UUID band_id;
    private UUID user_id;
    private UUID role_id;
    private UUID voice_id;


    @Override
    public void verifyNulls() {
        if (isNull(band_id)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado una banda", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (isNull(user_id)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado un usuario", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (isNull(role_id)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado un rol", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        if (isNull(voice_id)) {
            throw new GestBandaException(new Exception(), "No se ha proporcionado una voz", ErrorCodes.ARGUMENT_NOT_VALID);
        }
    }

    /**
     * Executes all validation steps, currently limited to verifying non-null fields.
     */
    @Override
    public void validate() {
        verifyNulls();
    }
}
