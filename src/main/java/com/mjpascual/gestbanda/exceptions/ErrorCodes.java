package com.mjpascual.gestbanda.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enum with the codes and descriptions of the exceptions thrown by the backend
 */
@AllArgsConstructor
@Getter
public enum ErrorCodes {

    GENERIC_ERROR(1, "GENERIC_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    BAND_NOT_FOUND(2, "BAND_NOT_FOUND", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(3, "USER_NOT_FOUND", HttpStatus.NOT_FOUND),
    ARGUMENT_NOT_VALID(4, "ARGUMENT_NOT_VALID", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(5, "EMAIL_ALREADY_EXISTS", HttpStatus.CONFLICT),
    PHONE_ALREADY_EXISTS(6, "PHONE_ALREADY_EXISTS", HttpStatus.BAD_REQUEST),
    READ_ERROR(7, "READ_ERROR", HttpStatus.BAD_REQUEST),
    UPLOAD_IMAGE_ERROR(8, "UPLOAD_IMAGE_ERROR", HttpStatus.FAILED_DEPENDENCY);

    private final int code;
    private final String message;
    private final HttpStatus status;
}
