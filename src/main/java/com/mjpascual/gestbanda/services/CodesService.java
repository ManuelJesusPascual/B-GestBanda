package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.codes.JoinBandRequest;
import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.dto.usersBand.CreateUsersBandRequest;
import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.entities.Codes;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.CodesRepository;
import com.mjpascual.gestbanda.repositories.RolesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * CodesService provides functionality related to band access codes,
 * allowing users to join bands using a specific code and handling
 * the associated business logic and validations.
 */
@Service
public class CodesService {

    private final CodesRepository codesRepository;
    private final UsersBandService usersBandService;
    private final UsersService usersService;

    /**
     * Constructs a new instance of CodesService with the necessary dependencies.
     *
     * @param codesRepository Repository for managing access codes
     * @param usersBandService Service for managing user-band associations
     * @param bandService Service for managing bands (not used in this service)
     * @param usersService Service for user-related operations
     */
    @Autowired
    public CodesService(CodesRepository codesRepository, UsersBandService usersBandService, BandsService bandService, UsersService usersService) {
        this.codesRepository = codesRepository;
        this.usersBandService = usersBandService;
        this.usersService = usersService;
    }

    /**
     * Retrieves a band associated with the specified code. If the code is not found
     * or has expired, a GestBandaException is thrown with the appropriate error code.
     *
     * @param code The code associated with the band to retrieve
     * @return BandsResponse containing information about the band linked to the code
     * @throws GestBandaException if the code is invalid or expired
     */
    @Transactional(rollbackOn = Exception.class)
    public BandsResponse getBandByCode(String code) {

        Codes codeInfo = codesRepository.findByCode(code);

        if (codeInfo == null) {
            throw new GestBandaException(new Exception(), "No se ha encontrado una banda con ese codigo asociado", ErrorCodes.CODE_NOT_FOUND);
        } else if (codeInfo.getEndDate().before(new Date())) {
            throw new GestBandaException(new Exception(), "Ese codigo ha expirado. Por favor contacta con el director de la banda", ErrorCodes.CODE_EXPIRED);
        }

        return BandsResponse.from(codeInfo.getBand_id());
    }

    /**
     * Allows a user to join a band by validating the code and user details.
     * After validating, it associates the user with the band in the system.
     *
     * @param request JoinBandRequest containing the code and user information
     * @return BandsResponse containing details of the band the user has joined
     * @throws GestBandaException if the code is invalid, expired, or user validation fails
     */
    @Transactional(rollbackOn = Exception.class)
    public BandsResponse joinBandByCode(JoinBandRequest request) {

        BandsResponse band = getBandByCode(request.getCode());
        UsersResponse user = usersService.checkUser(request.getUser());
        usersBandService.insertUserBand(band, user);

        return band;
    }
}
