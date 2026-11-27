package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.bands.CreateBandRequest;
import com.mjpascual.gestbanda.dto.users.CreateUserRequest;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.entities.Users;
import com.mjpascual.gestbanda.entities.UsersBand;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.isNull;

/**
 * Service class responsible for managing user-related operations,
 * including creation, validation, and retrieval of users in the system.
 */
@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new UsersService instance with dependencies injected.
     * Uses @Lazy annotation on passwordEncoder due to circular dependency with security configuration.
     *
     * @param usersRepository Repository for accessing user data
     * @param passwordEncoder Encoder for encoding user passwords
     */
    public UsersService(UsersRepository usersRepository, @Lazy PasswordEncoder passwordEncoder, UsersBandService usersBandService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user in the system.
     * Validates if the user already exists before creating.
     * Sets creation date and encodes the password.
     *
     * @param user CreateUserRequest containing user details
     * @return UsersResponse representing the newly created user
     */
    @Transactional(rollbackOn = Exception.class)
    public UsersResponse createUser(CreateUserRequest user) {
        validateCreateUser(user);

        Users userRegister = Users.from(user);
        userRegister.setPassword(passwordEncoder.encode(user.getPassword()));

        return UsersResponse.from(usersRepository.save(userRegister));
    }

    /**
     * Validates if a user already exists in the database by email and phone.
     * Uses asynchronous requests for validation.
     * Throws an exception if a duplicate email or phone is found.
     *
     * @param user CreateUserRequest representing the user to validate
     * @throws GestBandaException if the user already exists in the system
     */
    private void validateCreateUser(CreateUserRequest user) {
        try {
            CompletableFuture<Users> emailValidationFuture = CompletableFuture.supplyAsync(() -> usersRepository.findByEmail(user.getEmail().toLowerCase()));
            CompletableFuture<Users> phoneValidationFuture = CompletableFuture.supplyAsync(() -> usersRepository.findByPhone(user.getPhone().toLowerCase()));

            CompletableFuture<Void> allValidations = CompletableFuture.allOf(emailValidationFuture, phoneValidationFuture);
            allValidations.join(); // Wait for both validations to complete.

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
     * Retrieves login information for a user based on their email.
     *
     * @param mail Email address of the user to retrieve
     * @return UsersResponse containing user information
     */
    @Transactional(rollbackOn = Exception.class)
    public UsersResponse getLoginInfo(String mail) {
        Users user = usersRepository.findByEmail(mail);

        return UsersResponse.from(user);
    }

    /**
     * Finds and retrieves a user by their unique ID.
     * Throws an exception if the user is not found.
     *
     * @param id UUID of the user to find
     * @return UsersResponse representing the user's information
     */
    @Transactional(rollbackOn = Exception.class)
    public UsersResponse findUser(UUID id) {
        Users user = usersRepository.findById(id).orElseThrow(() -> new GestBandaException(new Exception(), "Usuario no encontrado", ErrorCodes.USER_NOT_FOUND));
        return UsersResponse.from(user);
    }

    /**
     * Checks the validity of a user based on a UsersResponse object.
     * Throws an exception if the user is invalid.
     *
     * @param usersResponse UsersResponse containing user details to validate
     * @return UsersResponse with confirmed user information
     */
    @Transactional
    public UsersResponse checkUser(UsersResponse usersResponse) {
        UsersResponse user;
        try {
            user = findUser(usersResponse.getId());
        } catch (Exception e) {
            throw new GestBandaException(new Exception(), "El usuario que intenta crear la banda no es válido", ErrorCodes.ARGUMENT_NOT_VALID);
        }
        return user;
    }


}
