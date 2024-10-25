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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.isNull;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, @Lazy PasswordEncoder passwordEncoder) { // The Lazy is added because PasswordEncoder is part of the security configuration.
        this.usersRepository = usersRepository;                                                  // The security layer needs UsersService to get information after a user logs in.
        this.passwordEncoder = passwordEncoder;                                                   // And UsersService needs the security layer to use the password encoder, so it is injected later.

    }

    /**
     * Method that creates a new user.
     * First, it will call the method validateCreateUser() to check if this user already exists in the database.
     * If it does not exist, it will call the database to create the user; if it exists, the validateCreateUser() method will throw an exception.
     * Before creating the user, it sets the creation date and encodes the password.
     * @return Created user.
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
     * Method that validates if the user already exists in the database.
     * It performs requests to the database simultaneously.
     * If the requests return results, it throws an exception indicating that the user already exists.
     * Otherwise, it continues with the process.
     *
     * @param user User to create.
     */
    private void validateCreateUser(CreateUserRequest user) {
        try {
            CompletableFuture<Users> emailValidationFuture = CompletableFuture.supplyAsync(() -> usersRepository.findByEmail(user.getEmail().toLowerCase()));
            CompletableFuture<Users> phoneValidationFuture = CompletableFuture.supplyAsync(() -> usersRepository.findByPhone(user.getPhone().toLowerCase()));

            CompletableFuture<Void> allValidations = CompletableFuture.allOf(emailValidationFuture, phoneValidationFuture);
            allValidations.join(); // Wait for both users to be fetched.

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
     * Method that retrieves the information of a user for login.
     * @param mail Email of the user to retrieve.
     * @return User information.
     */
    @Transactional(rollbackOn = Exception.class)
    public UsersResponse getLoginInfo(String mail) {
        Users user = usersRepository.findByEmail(mail);
        return UsersResponse.from(user);
    }

    /**
     * Finds a user by their unique ID, returning a response object if the user exists.
     * Throws a GestBandaException if the user is not found.
     *
     * @param id The UUID of the user to be searched.
     * @return UsersResponse containing the user's information.
     */
    @Transactional
    public UsersResponse findUser(UUID id) {
        Users user = usersRepository.findById(id).orElseThrow(() ->  new GestBandaException(new Exception(), "Usuario no encontrado", ErrorCodes.USER_NOT_FOUND));
        return UsersResponse.from(user);
    }

}
