package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.voices.AddVoiceToBandRequest;
import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import com.mjpascual.gestbanda.entities.Voices;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.RolesRepository;
import com.mjpascual.gestbanda.repositories.VoicesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service class for managing voice-related operations.
 * This class interacts with the VoicesRepository to perform CRUD operations
 * and business logic related to voice entities.
 */
@Service
public class VoicesService {

    private final VoicesRepository voicesRepository;

    /**
     * Constructor for VoicesService.
     *
     * @param voicesRepository The repository to manage voice entities.
     * @param usersBandService The service to manage users associated with bands.
     */
    @Autowired
    public VoicesService(VoicesRepository voicesRepository, UsersBandService usersBandService) {
        this.voicesRepository = voicesRepository;
    }

    /**
     * Finds a voice by its name.
     *
     * @param name The name of the voice to be found.
     * @return A VoicesResponse object containing the voice details.
     */
    public VoicesResponse findVoiceByName(String name) {
        VoicesResponse response;
        Voices voice = voicesRepository.findVoiceByName(name);
        response = VoicesResponse.from(voice);
        return response;
    }

    /**
     * Finds a voice by its unique identifier.
     *
     * @param id The UUID of the voice to be found.
     * @return A VoicesResponse object containing the voice details.
     * @throws GestBandaException if the voice is not found.
     */
    public VoicesResponse findVoiceById(UUID id) {
        VoicesResponse response;
        Voices voice = voicesRepository.findById(id)
                .orElseThrow(() -> new GestBandaException(new Exception(), "Voz no encontrada", ErrorCodes.BAND_NOT_FOUND));
        response = VoicesResponse.from(voice);
        return response;
    }
}
