package com.mjpascual.gestbanda.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


/**
 * DTO para los detalles de las excepciones. Será el DTO que devolveremos al usuario.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetailed {

    private Date timeMark;
    private String message;
    private int code;


}
