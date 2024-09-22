package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.bands.BandsResponse;
import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.BandsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Class that implements the methods of the BandsService interface.
 */
@Service
public class BandsService {

    /**
     * Repository for the Bands entity.
     */
    private final BandsRepository bandsRepository;

    @Autowired
    public BandsService(BandsRepository bandsRepository) {
        this.bandsRepository = bandsRepository;
    }

    /**
     * Method that returns a band by its ID.
     *
     * @param id ID of the band to search for.
     * @return Band matching the provided ID.
     * @throws GestBandaException Exception thrown if the band is not found.
     */
    public BandsResponse getBandById(UUID id) throws GestBandaException {
        return BandsResponse.from(bandsRepository.findById(id).orElseThrow(() -> GestBandaException.from(ErrorCodes.BAND_NOT_FOUND)));
    }

    /**
     * Method that creates a new band.
     *
     * @return Created band.
     */
    @Transactional(rollbackOn = Exception.class)
    public BandsResponse createBand(Bands band) {
        return BandsResponse.from(bandsRepository.save(band));
    }

    /**
     * Method that updates a band.
     *
     * @param id   ID of the band to update.
     * @param band Band with the new data.
     * @return Updated band.
     */
    public BandsResponse updateBand(UUID id, Bands band) {
        Bands oldBand = bandsRepository.findById(id).orElseThrow(() -> GestBandaException.from(ErrorCodes.BAND_NOT_FOUND));
        oldBand.setUpdated_at(new Date());
        Bands updatedBand = bandsRepository.save(oldBand);
        return BandsResponse.from(updatedBand);
    }

    /**
     * Method that deletes a band.
     *
     * @param id ID of the band to delete.
     */
    public void deleteBand(UUID id) {
        bandsRepository.deleteById(id);
    }
}
