package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.usersBand.BandsVoicesResponse;
import com.mjpascual.gestbanda.dto.usersBand.CreateUsersBandRequest;
import com.mjpascual.gestbanda.dto.voices.AddVoiceToBandRequest;
import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import com.mjpascual.gestbanda.entities.BandVoices;
import com.mjpascual.gestbanda.entities.UsersBand;
import com.mjpascual.gestbanda.entities.Voices;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.BandVoicesRepository;
import com.mjpascual.gestbanda.repositories.VoicesRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

/**
 * Service class for managing the relationship between bands and their voices.
 * This service handles operations such as adding or removing voices from bands,
 * as well as retrieving voices associated with a band.
 */
@Service
public class BandVoicesService {

    private final BandVoicesRepository bandVoicesRepository;
    private final VoicesService voicesService;
    private final BandsService bandsService;
    private final VoicesRepository voicesRepository;

    /**
     * Constructor for BandVoicesService.
     * Initializes the repositories and services required for managing band voices.
     *
     * @param bandVoicesRepository the repository for performing CRUD operations on band voices
     * @param voicesService        the service for managing voices
     * @param bandsService         the service for managing bands
     * @param voicesRepository     the repository for performing CRUD operations on voices
     */
    public BandVoicesService(BandVoicesRepository bandVoicesRepository, VoicesService voicesService, BandsService bandsService, VoicesRepository voicesRepository) {
        this.bandVoicesRepository = bandVoicesRepository;
        this.voicesService = voicesService;
        this.bandsService = bandsService;
        this.voicesRepository = voicesRepository;
    }

    /**
     * Adds a voice to a specified band.
     *
     * @param request the request containing the voice name and band UUID
     * @return a response containing the updated band voices information
     */
    @Transactional(rollbackOn = Exception.class)
    public VoicesResponse addVoiceToBand(AddVoiceToBandRequest request) {
        VoicesResponse voice = voicesService.findVoiceByName(request.getVoice_name());
        BandsResponse band = bandsService.findBandById(request.getBand_uuid());
        BandVoices bandVoice = BandVoices.from(voice, band);
        checkBandVoice(bandVoice);
        BandVoices response = bandVoicesRepository.save(bandVoice);
        return VoicesResponse.from(response.getId_voice());
    }

    /**
     * Retrieves all voices associated with a specified band.
     *
     * @param id the UUID of the band
     * @return a list of voices associated with the band
     */
    @Transactional(rollbackOn = Exception.class)
    public List<VoicesResponse> getVoicesBand(UUID id) {
        List<BandVoices> bandVoices = bandVoicesRepository.findAllBandVoices(id);
        List<VoicesResponse> responses = VoicesResponse.fromBandVoices(bandVoices);
        return responses;
    }

    /**
     * Retrieves all voices that are not associated with a specified band.
     *
     * @param id the UUID of the band
     * @return a list of voices not associated with the band
     */
    @Transactional(rollbackOn = Exception.class)
    public List<VoicesResponse> getVoicesBandNotHave(UUID id) {
        List<Voices> voices = voicesRepository.findVoicesBandNotHave(id);
        List<VoicesResponse> responses = VoicesResponse.fromVoices(voices);
        return responses;
    }

    /**
     * Checks if a specific band voice already exists in the repository.
     * Throws an exception if the voice is already associated with the band.
     *
     * @param bandVoice the band voice to check
     */
    private void checkBandVoice(BandVoices bandVoice) {
        BandVoices exist = bandVoicesRepository.checkIfExist(bandVoice.getId_voice().getId(), bandVoice.getId_band().getId());
        if (!isNull(exist)) {
            throw new GestBandaException(new Exception(),"Esa voz ya existe en esta banda", ErrorCodes.ARGUMENT_NOT_VALID);
        }
    }

    public BandVoices voiceExistInBand( UUID voice_id,UUID band_id) {
        BandVoices exist = bandVoicesRepository.checkIfExist(voice_id, band_id);
        if(isNull(exist)) {
            throw new GestBandaException(new Exception(),"Esa voz no existe en esa banda", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        return exist;
    }

    /**
     * Deletes a specific voice from a band.
     *
     * @param idVoice the UUID of the voice to delete
     * @param idBand  the UUID of the band from which to delete the voice
     */
    @Transactional(rollbackOn = Exception.class)
    public void deleteVoiceBand(UUID idVoice, UUID idBand) {
        BandVoices bandVoices = bandVoicesRepository.checkIfExist(idVoice, idBand);
        if (isNull(bandVoices)) {
            throw new GestBandaException(new Exception(),"No se ha encontrado esa voz en la banda", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        resetToDefaultVoice(idVoice,idBand);
        bandVoicesRepository.delete(bandVoices);
    }

    private void resetToDefaultVoice(UUID idVoice,UUID idBand) {

        Voices sinVoz = voicesRepository.getNoVoice();
        voicesRepository.setCodesVoicesToDefault(sinVoz.getId(),idVoice,idBand,new Date());
        voicesRepository.setUserVoicesToDefault(sinVoz.getId(),idVoice,idBand,new Date());

    }
}
