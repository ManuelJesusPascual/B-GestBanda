package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.dto.bands.CreateBandRequest;
import com.mjpascual.gestbanda.dto.img.ImgResponse;
import com.mjpascual.gestbanda.dto.roles.RolesResponse;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.dto.usersBand.CreateUsersBandRequest;
import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.entities.Roles;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.BandsRepository;
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

    private final BandsRepository bandsRepository;

    private final UsersBandService usersBandService;

    private final UsersService usersService;

    private final ImgService imgService;


    /**
     * Constructor with dependency injection for various services required for band management.
     *
     * @param bandsRepository Repository for Bands entity.
     * @param usersBandService Service for managing users in bands.
     * @param usersService Service for managing users.
     * @param imgService Service for managing images.
     * @param rolesService Service for managing roles.
     */
    @Autowired
    public BandsService(BandsRepository bandsRepository, UsersBandService usersBandService, UsersService usersService, ImgService imgService, RolesService rolesService) {
        this.bandsRepository = bandsRepository;
        this.usersBandService = usersBandService;
        this.usersService = usersService;
        this.imgService = imgService;
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
        UsersResponse user;
        user = usersService.checkUser(band.getUser());

        ImgResponse imgResponse = imgService.uploadImage(img);

        Bands bandToSave = Bands.from(band, imgResponse);

        BandsResponse savedBand = BandsResponse.from(bandsRepository.save(bandToSave));

        usersBandService.insertUserBand(savedBand, user);

        return savedBand;
    }

    /**
     * Finds a band by its unique identifier.
     *
     * @param bandUuid the unique identifier of the band to be found
     * @return a BandsResponse object containing the details of the found band
     * @throws GestBandaException if no band is found with the given UUID
     */
    public BandsResponse findBandById(UUID bandUuid) {
        Bands band = bandsRepository.findById(bandUuid).orElseThrow(() -> new GestBandaException(new Exception(), "Banda no encontrada", ErrorCodes.BAND_NOT_FOUND));
        return BandsResponse.from(band);
    }

}
