package com.mjpascual.gestbanda.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


/**
 * DTO para los detalles de las excepciones
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetailed {

    private Date timeMark;
    private Exception error;
    private String message;
    private ErrorCodes code;


}
