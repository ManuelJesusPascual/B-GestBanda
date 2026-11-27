package com.mjpascual.gestbanda.dto.usersBand;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
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
public class CreateUsersBandRequest implements IValidator {

    private BandsResponse band_id;    // Represents the band associated with the user-band relationship
    private UsersResponse user_id;    // Represents the user associated with the band
    private RolesResponse role_id;    // Represents the role of the user in the band
    private VoicesResponse voice_id; // Represents the voice of the user in the band

    /**
     * Verifies that the required fields are not null. Throws a GestBandaException if
     * any of the fields (band_id, user_id, role_id) is missing.
     */
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
    }

    /**
     * Executes all validation steps, currently limited to verifying non-null fields.
     */
    @Override
    public void validate() {
        verifyNulls();
    }
}
