package com.mjpascual.gestbanda.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodes {

    GENERIC_ERROR(1,"GENERIC_ERROR"),
    BAND_NOT_FOUND(2,"BAND_NOT_FOUND"),
    USER_NOT_FOUND(3,"USER_NOT_FOUND");


    private final int code;
    private final String message;
}
