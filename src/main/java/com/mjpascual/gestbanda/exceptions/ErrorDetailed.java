package com.mjpascual.gestbanda.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * DTO for exception details. This will be the DTO returned to the user.
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
