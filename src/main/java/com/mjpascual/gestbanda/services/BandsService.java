package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.entities.Bands;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.BandsRepository;
import com.mjpascual.gestbanda.repositories.UserRoleInBandRepository;
import com.mjpascual.gestbanda.repositories.UsersBandRepository;
import com.mjpascual.gestbanda.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Clase que implementa los métodos de la interfaz BandsService
 */
@Service
public class BandsService  {

    /**
     * Repositorio de la entidad Bands
     */
    private final BandsRepository bandsRepository;

    private final UsersRepository usersRepository;

    private final UserRoleInBandRepository userRoleInBandRepository;

    private final UsersBandRepository usersInBandRepository;
    @Autowired
    public BandsService(BandsRepository bandsRepository, UsersRepository usersRepository, UserRoleInBandRepository userRoleInBandRepository, UsersBandRepository usersInBandRepository) {
        this.bandsRepository = bandsRepository;
        this.usersRepository = usersRepository;
        this.userRoleInBandRepository = userRoleInBandRepository;
        this.usersInBandRepository = usersInBandRepository;
    }

    /**
     * Método que devuelve una banda por su id
     * @param id ID de la banda a buscar
     * @throws GestBandaException Excepción que se lanzará en caso de no encontrar la banda
     * @return Banda coincidente con el id introducido por parámetros.
     */

    public Bands getBandById(UUID id) throws GestBandaException {
        return bandsRepository.findById(id).orElseThrow(() -> GestBandaException.from(ErrorCodes.BAND_NOT_FOUND));

    }

    /**
     * Método que crea una nueva banda
     * @return Banda creada
     */

    @Transactional(rollbackOn = Exception.class)
    public Bands createBand (Bands band){

        return bandsRepository.save(band);

    }

    /**
     * Método que actualiza una banda
     * @param id ID de la banda a actualizar
     * @param band Banda con los nuevos datos
     * @return Banda actualizada
     */

    public Bands updateBand(UUID id, Bands band) {
        Bands oldBand = bandsRepository.findById(id).orElseThrow(() -> GestBandaException.from(ErrorCodes.BAND_NOT_FOUND));
        oldBand.setUpdated_at(new Date());
        Bands updatedBand = bandsRepository.save(oldBand);
        return updatedBand;
    }

    /**
     * Método que elimina una banda
     * @param id ID de la banda a eliminar
     */

    public void deleteBand(UUID id){
            bandsRepository.deleteById(id);


    }



}
