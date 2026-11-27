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

    private final CodesRepository codesRepository;
    private final UsersBandService usersBandService;
    private final VoicesService voicesService;
    private final BandAccessService bandAccessService;
    private final BandsService bandsService;
    private final BandVoicesService bandVoicesService;

    /**
     * Constructs a new instance of CodesService with the necessary dependencies.
     *
     * @param codesRepository  Repository for managing access codes
     * @param usersBandService Service for managing user-band associations
     * @param voicesService    Service for searching voices
     * @param bandAccessService Checks what the current user can do in a band
     * @param bandsService     Service for managing bands
     * @param bandVoicesService Service for managing band voices
     */
    @Autowired
    public CodesService(CodesRepository codesRepository, UsersBandService usersBandService, VoicesService voicesService, BandAccessService bandAccessService, BandsService bandsService, BandVoicesService bandVoicesService) {
        this.codesRepository = codesRepository;
        this.usersBandService = usersBandService;
        this.voicesService = voicesService;
        this.bandAccessService = bandAccessService;
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

        Codes codeInfo = codesRepository.findByCode(code);

        // Check if the code is null (not found) and throw an exception if so
        if (codeInfo == null) {
            throw new GestBandaException(new Exception(), "No se ha encontrado una banda con ese codigo asociado", ErrorCodes.CODE_NOT_FOUND);
        }
        // Check if the code has expired and throw an exception if so
        else if (codeInfo.getEndDate().before(new Date())) {
            throw new GestBandaException(new Exception(), "Ese codigo ha expirado. Por favor contacta con el director de la banda", ErrorCodes.CODE_EXPIRED);
        }

        return codeInfo;
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

        Codes codeInfo = getCode(request.getCode());
        BandsResponse band = BandsResponse.from(codeInfo.getBand_id());
        VoicesResponse voice = VoicesResponse.from(codeInfo.getVoice_id());

        UsersResponse user = bandAccessService.currentUser(); // request.getUser() is ignored
        usersBandService.insertUserBand(band, voice, user, UsersBandService.ROLE_MEMBER);

        return band;
    }

    /**
     * Retrieves a list of access codes associated with a specific band.
     *
     * @param bandID The ID of the band for which to retrieve codes
     * @return List of CodesResponse containing codes associated with the band
     */
    @Transactional(rollbackOn = Exception.class)
    public List<CodesResponse> getBandCodes(UUID bandID) {
        bandAccessService.requireDirector(bandID);
        List<Codes> bandCodes = codesRepository.findBandCodes(bandID);
        return CodesResponse.from(bandCodes);
    }

    /**
     * Deletes a band code by its ID.
     *
     * @param idCode The ID of the code to be deleted
     * @throws GestBandaException if the code does not exist
     */
    @Transactional(rollbackOn = Exception.class)
    public void deleteBandCode(UUID idCode) {
        Codes code = codesRepository.checkIfExist(idCode);
        // If the code is null, throw an exception indicating it was not found
        if (isNull(code)) {
            throw new GestBandaException(new Exception(), "No se ha encontrado ese código en la banda", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        bandAccessService.requireDirector(code.getBand_id().getId());
        codesRepository.delete(code);
    }

    /**
     * Creates a new band code with the provided request details.
     *
     * @param request CreateCodeRequest containing the details for the new code
     * @return CodesResponse containing the details of the created code
     */
    public CodesResponse createBandCode(CreateCodeRequest request) {
        bandAccessService.requireDirector(request.getBand_id());
        VoicesResponse voice = null;
        BandsResponse band = bandsService.findBandById(request.getBand_id());
        voice = voicesService.findVoiceById(request.getVoice_id());

        Codes code;

        Codes codeExist;
        // Generate a new code and check for duplicates
        do {
            code = Codes.from(request, voice, band);
            codeExist = codesRepository.checkIfExistByCode(code.getCode());
        } while (!isNull(codeExist));

        Codes savedCode = codesRepository.save(code);
        return CodesResponse.from(savedCode);
    }

    /**
     * Updates an existing band code with the provided request details.
     *
     * @param request UpdateCodeRequest containing the details for the update
     * @return CodesResponse containing the updated code details
     * @throws GestBandaException if the code is not found
     */
    public CodesResponse updateBandCode(UpdateCodeRequest request) {

        Codes code;

        // Fetch the existing code by ID, throwing an exception if not found
        code = codesRepository.findById(request.getId()).orElseThrow(() -> new GestBandaException(new Exception(), "Código no encontrado", ErrorCodes.CODE_NOT_FOUND));
        UUID bandId = code.getBand_id().getId();
        bandAccessService.requireDirector(bandId);

        Voices voice;

        // Check if the voice exists in the band and get its ID
        voice = bandVoicesService.voiceExistInBand(request.getVoice_id(), bandId).getId_voice();

        code.setVoice_id(voice);
        code.setUpdated_at(new Date());
        code.setEndDate(request.getEnd_date());

        Codes savedCode = codesRepository.save(code);
        return CodesResponse.from(savedCode);
    }
}
