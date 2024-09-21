package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.users.CreateUserRequest;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.entities.Users;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.isNull;


@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, @Lazy PasswordEncoder passwordEncoder) { //Se añade el Lazy porque PasswordEncoder es del security.
        this.usersRepository = usersRepository;                                                   //Al security le hace falta usersService para obtener la informacion tras un login de un usuario
        this.passwordEncoder = passwordEncoder;                                                   //Y al UsersService le hace falta el security para usar el passwordencoder, entonces se hace que se injecte mas tarde.
    }

    /**
     * Método que crea un nuevo usuario.
     * Primero se llamará al método validateCreateUser() para verificar si este usuario ya existe en la BBDD.
     * Si no existe, se llamará a la BBDD para crear el usuario, si existe, el propio método validateCreateUser(), lanzará la excepción.
     * Antes de crear el usuario, inserta la fecha de creación y codifica su contraseña.
     * @return Usuario creado
     */
    @Transactional(rollbackOn = Exception.class)
    public UsersResponse createUser(CreateUserRequest user) {

        validateCreateUser(user);


        Users userRegister = Users.from(user);
        userRegister.setCreated_at(new Date());
        userRegister.setPassword(passwordEncoder.encode(user.getPassword()));

        return UsersResponse.from(usersRepository.save(userRegister));


    }

    /**
     * Método que validará si el usuario ya existe en la BBDD, realizará las peticiones a la misma vez a la BBDD
     * Tras eso, si las peticiones devuelven resultados, lanzará una excepción de que el usuario ya existe.
     * De lo contrario, continuará con el proceso.
     *
     * @param user Usuario a crear
     */
    private void validateCreateUser(CreateUserRequest user) {
        try {
            CompletableFuture<Users> emailValidationFuture = CompletableFuture.supplyAsync(() -> usersRepository.findByEmail(user.getEmail().toLowerCase()));
            CompletableFuture<Users> phoneValidationFuture = CompletableFuture.supplyAsync(() -> usersRepository.findByPhone(user.getPhone().toLowerCase()));

            CompletableFuture<Void> allValidations = CompletableFuture.allOf(emailValidationFuture, phoneValidationFuture);
            allValidations.join(); //Espera a que se hayan obtenido ambos usuarios


            Users userAlreadyExistsByEmail = emailValidationFuture.get();
            Users userAlreadyExistsByPhone = phoneValidationFuture.get();
            if (!isNull(userAlreadyExistsByEmail)) {
                throw new GestBandaException(new Exception(), "Se ha intentado registrar un usuario con un email ya existente", ErrorCodes.EMAIL_ALREADY_EXISTS);
            }

            if (!isNull(userAlreadyExistsByPhone)) {
                throw new GestBandaException(new Exception(), "Se ha intentado registrar un usuario con un telefono ya existente", ErrorCodes.PHONE_ALREADY_EXISTS);
            }

        } catch (InterruptedException | ExecutionException e) {
            throw new GestBandaException(new Exception(), "Error al validar el usuario", ErrorCodes.GENERIC_ERROR);
        }
    }

    /**
     * Método que obtendrá la información de un usuario para loguearlo.
     * @param mail Email del usuario a obtener
     * @return Información del usuario.
     */
    @Transactional(rollbackOn = Exception.class)
    public UsersResponse getLoginInfo(String mail) {

        Users user = usersRepository.findByEmail(mail);

        return UsersResponse.from(user);
    };

}
