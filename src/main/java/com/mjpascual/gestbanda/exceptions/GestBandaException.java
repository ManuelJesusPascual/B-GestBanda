package com.mjpascual.gestbanda.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Excepción genérica para la aplicación
 */
@Getter
@Setter
@NoArgsConstructor
@Builder
public class GestBandaException extends RuntimeException {


    private static final long serialVersionUID = 1L;

    private Exception error; // Error Exception (Generalmente siempre lanzaremos Exception())
    private String message; // Mensaje descriptivo de la excepcion
    private ErrorCodes code; //Numero propio error personalizado, @see ErrorCodes

    public GestBandaException(Exception error, String message, ErrorCodes code) {
        super(message);
        this.error = error;
        this.message = message;
        this.code = code;
    }

    public GestBandaException from(Exception error, String message) {
        return GestBandaException.builder().error(error).message(message).build();
    }

    public GestBandaException from(Exception error, String message, ErrorCodes errorCode) {
        return GestBandaException.builder()
                .error(error)
                .message(message)
                .code(errorCode)
                .build();
    }

    public static GestBandaException from(Exception error, ErrorCodes errorCode) {
        return GestBandaException.builder()
                .error(error)
                .message(errorCode.getMessage())
                .code(errorCode)
                .build();
    }

    public static GestBandaException from(ErrorCodes errorCode) {
        return GestBandaException.builder()
                .message(errorCode.getMessage())
                .code(errorCode)
                .build();
    }


}

