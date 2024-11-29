package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.bands.CreateBandRequest;
import com.mjpascual.gestbanda.dto.img.ImgResponse;
import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.dto.usersBand.CreateUsersBandRequest;
import com.mjpascual.gestbanda.dto.voices.AddVoiceToBandRequest;
import com.mjpascual.gestbanda.dto.voices.VoicesResponse;
import com.mjpascual.gestbanda.entities.BandVoices;
import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.entities.Roles;
import com.mjpascual.gestbanda.entities.Voices;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.BandVoicesRepository;
import com.mjpascual.gestbanda.repositories.BandsRepository;
import com.mjpascual.gestbanda.repositories.VoicesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

import static java.util.Objects.isNull;

/**
 * Service class that manages band-related operations, such as creating new bands,
 * handling user-band relationships, and managing image uploads.
 */
@Service
public class BandsService {

    private final BandsRepository bandsRepository; // Repository for Bands entity

    private final UsersBandService usersBandService; // Service for managing users in bands

    private final UsersService usersService; // Service for managing users

    private final ImgService imgService; // Service for managing images
    private final VoicesRepository voicesRepository; // Repository for Voices entity
    private final BandVoicesRepository bandVoicesRepository; // Repository for BandVoices entity


    /**
     * Constructor with dependency injection for various services required for band management.
     *
     * @param bandsRepository Repository for Bands entity.
     * @param usersBandService Service for managing users in bands.
     * @param usersService Service for managing users.
     * @param imgService Service for managing images.
     */
    @Autowired
    public BandsService(BandsRepository bandsRepository, UsersBandService usersBandService, UsersService usersService, ImgService imgService, VoicesRepository voicesRepository, BandVoicesRepository bandVoicesRepository) {
        this.bandsRepository = bandsRepository; // Initializing bands repository
        this.usersBandService = usersBandService; // Initializing users band service
        this.usersService = usersService; // Initializing users service
        this.imgService = imgService; // Initializing image service
        this.voicesRepository = voicesRepository; // Initializing voices repository
        this.bandVoicesRepository = bandVoicesRepository; // Initializing band voices repository
    }

    /**
     * Creates a new band, assigns a user as the director, and manages the image upload process.
     *
     * @param band Request object containing details to create the band.
     * @param img Multipart file representing the band's image.
     * @return The response object with details of the newly created band.
     */
    @Transactional(rollbackOn = Exception.class)
    public BandsResponse createBand(CreateBandRequest band, MultipartFile img) {
        // Check if the user exists in the database
        UsersResponse user;
        user = usersService.checkUser(band.getUser());

        // Upload image to the image server (in this case imgur)
        ImgResponse imgResponse = imgService.uploadImage(img);

        // Save the band in the database
        Bands bandToSave = Bands.from(band, imgResponse);

        BandsResponse savedBand = BandsResponse.from(bandsRepository.save(bandToSave));

        // Get the default voice to save it
        VoicesResponse sinVoz = VoicesResponse.from(voicesRepository.getNoVoice());

        BandVoices bandVoice = BandVoices.from(sinVoz, savedBand);
        bandVoicesRepository.save(bandVoice);

        // Insert the user into the band
        usersBandService.insertUserBand(savedBand, sinVoz, user);

        return savedBand; // Return the saved band response
    }

    /**
     * Finds a band by its unique identifier.
     *
     * @param bandUuid the unique identifier of the band to be found
     * @return a BandsResponse object containing the details of the found band
     * @throws GestBandaException if no band is found with the given UUID
     */
    @Transactional(rollbackOn = Exception.class)
    public BandsResponse findBandById(UUID bandUuid) {
        Bands band = bandsRepository.findById(bandUuid).orElseThrow(() -> new GestBandaException(new Exception(), "Banda no encontrada", ErrorCodes.BAND_NOT_FOUND));
        return BandsResponse.from(band); // Return the found band response
    }

}
