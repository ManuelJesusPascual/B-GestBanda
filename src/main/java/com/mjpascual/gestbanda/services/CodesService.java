package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.codes.CodesResponse;
import com.mjpascual.gestbanda.dto.codes.CreateCodeRequest;
import com.mjpascual.gestbanda.dto.codes.JoinBandRequest;
import com.mjpascual.gestbanda.dto.codes.UpdateCodeRequest;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import com.mjpascual.gestbanda.entities.Codes;
import com.mjpascual.gestbanda.entities.Voices;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.CodesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

/**
 * CodesService provides functionality related to band access codes,
 * allowing users to join bands using a specific code and handling
 * the associated business logic and validations.
 */
@Service
public class CodesService {

    private final CodesRepository codesRepository; // Repository for managing access codes
    private final UsersBandService usersBandService; // Service for managing user-band associations
    private final VoicesService voicesService; // Service for searching voices
    private final UsersService usersService; // Service for user-related operations
    private final BandsService bandsService; // Service for managing bands
    private final BandVoicesService bandVoicesService; // Service for managing band voices

    /**
     * Constructs a new instance of CodesService with the necessary dependencies.
     *
     * @param codesRepository  Repository for managing access codes
     * @param usersBandService Service for managing user-band associations
     * @param voicesService    Service for searching voices
     * @param usersService     Service for user-related operations
     * @param bandsService     Service for managing bands
     * @param bandVoicesService Service for managing band voices
     */
    @Autowired
    public CodesService(CodesRepository codesRepository, UsersBandService usersBandService, VoicesService voicesService, UsersService usersService, BandsService bandsService, BandVoicesService bandVoicesService) {
        this.codesRepository = codesRepository;
        this.usersBandService = usersBandService;
        this.voicesService = voicesService;
        this.usersService = usersService;
        this.bandsService = bandsService;
        this.bandVoicesService = bandVoicesService;
    }

    /**
     * Retrieves a band associated with the specified code. If the code is not found
     * or has expired, a GestBandaException is thrown with the appropriate error code.
     *
     * @param code The code associated with the band to retrieve
     * @return Codes containing information about the code linked to the band
     * @throws GestBandaException if the code is invalid or expired
     */
    @Transactional(rollbackOn = Exception.class)
    public Codes getCode(String code) {

        Codes codeInfo = codesRepository.findByCode(code); // Fetch code information from the repository

        // Check if the code is null (not found) and throw an exception if so
        if (codeInfo == null) {
            throw new GestBandaException(new Exception(), "No se ha encontrado una banda con ese codigo asociado", ErrorCodes.CODE_NOT_FOUND);
        }
        // Check if the code has expired and throw an exception if so
        else if (codeInfo.getEndDate().before(new Date())) {
            throw new GestBandaException(new Exception(), "Ese codigo ha expirado. Por favor contacta con el director de la banda", ErrorCodes.CODE_EXPIRED);
        }

        return codeInfo; // Return the found code information
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

        Codes codeInfo = getCode(request.getCode()); // Validate the code and get associated info
        BandsResponse band = BandsResponse.from(codeInfo.getBand_id()); // Create BandsResponse from band ID
        VoicesResponse voice = VoicesResponse.from(codeInfo.getVoice_id()); // Create VoicesResponse from voice ID

        UsersResponse user = usersService.checkUser(request.getUser()); // Validate the user
        usersBandService.insertUserBand(band, voice, user); // Associate user with the band

        return band; // Return the band that the user has joined
    }

    /**
     * Retrieves a list of access codes associated with a specific band.
     *
     * @param bandID The ID of the band for which to retrieve codes
     * @return List of CodesResponse containing codes associated with the band
     */
    @Transactional(rollbackOn = Exception.class)
    public List<CodesResponse> getBandCodes(UUID bandID) {
        List<Codes> bandCodes = codesRepository.findBandCodes(bandID); // Fetch codes for the specified band
        return CodesResponse.from(bandCodes); // Return the list of codes as CodesResponse
    }

    /**
     * Deletes a band code by its ID.
     *
     * @param idCode The ID of the code to be deleted
     * @throws GestBandaException if the code does not exist
     */
    @Transactional(rollbackOn = Exception.class)
    public void deleteBandCode(UUID idCode) {
        Codes code = codesRepository.checkIfExist(idCode); // Check if the code exists
        // If the code is null, throw an exception indicating it was not found
        if (isNull(code)) {
            throw new GestBandaException(new Exception(), "No se ha encontrado ese código en la banda", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        codesRepository.delete(code); // Delete the found code from the repository
    }

    /**
     * Creates a new band code with the provided request details.
     *
     * @param request CreateCodeRequest containing the details for the new code
     * @return CodesResponse containing the details of the created code
     */
    public CodesResponse createBandCode(CreateCodeRequest request) {
        VoicesResponse voice = null; // Initialize the voice response
        BandsResponse band = bandsService.findBandById(request.getBand_id()); // Get the band by ID
        voice = voicesService.findVoiceById(request.getVoice_id()); // Get the voice by ID

        Codes code; // Variable to hold the new code

        Codes codeExist; // Variable to check for existing codes
        // Generate a new code and check for duplicates
        do {
            code = Codes.from(request, voice, band); // Create a new code from the request
            codeExist = codesRepository.checkIfExistByCode(code.getCode()); // Check if the code already exists
        } while (!isNull(codeExist)); // Continue until a unique code is generated

        Codes savedCode = codesRepository.save(code); // Save the new code to the repository
        return CodesResponse.from(savedCode); // Return the saved code as CodesResponse
    }

    /**
     * Updates an existing band code with the provided request details.
     *
     * @param request UpdateCodeRequest containing the details for the update
     * @return CodesResponse containing the updated code details
     * @throws GestBandaException if the code is not found
     */
    public CodesResponse updateBandCode(UpdateCodeRequest request) {

        Codes code; // Variable to hold the code to be updated

        // Fetch the existing code by ID, throwing an exception if not found
        code = codesRepository.findById(request.getId()).orElseThrow(() -> new GestBandaException(new Exception(), "Código no encontrado", ErrorCodes.CODE_NOT_FOUND));

        Voices voice; // Variable to hold the voice to be associated with the code

        // Check if the voice exists in the band and get its ID
        voice = bandVoicesService.voiceExistInBand(request.getVoice_id(), request.getBand_id()).getId_voice();

        code.setVoice_id(voice); // Set the new voice ID on the code
        code.setUpdated_at(new Date()); // Update the timestamp for the code
        code.setEndDate(request.getEnd_date()); // Set the new end date for the code

        Codes savedCode = codesRepository.save(code); // Save the updated code to the repository
        return CodesResponse.from(savedCode); // Return the updated code as CodesResponse
    }
}
