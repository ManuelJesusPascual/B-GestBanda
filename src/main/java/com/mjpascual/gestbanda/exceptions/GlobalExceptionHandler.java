package com.mjpascual.gestbanda.exceptions;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Clase que maneja las excepciones de la aplicación
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * Método que maneja las excepciones de la app (GestBandaException)
     * @param exception  Excepción lanzada por la aplicación
     * @param webRequest Petición web
     * @return ResponseEntity con los detalles de la excepción
     * @see GestBandaException Clase que define el mensaje de la excepción
     * @see ErrorDetailed Clase que contiene los detalles de la excepción.
     */
    @ExceptionHandler(GestBandaException.class)
    public ResponseEntity<ErrorDetailed> handleMainException(
            GestBandaException exception, WebRequest webRequest) {
        ErrorDetailed errorDetails =
                new ErrorDetailed(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime(), exception.getMessage(),
                        exception.getCode().getCode());

        return new ResponseEntity<>(errorDetails, exception.getCode().getStatus());
    }

    /**
     * Método que maneja la excepción de validación de argumentos
     * @param ex      Excepción lanzada por la aplicación (throw new MethodArgumentNotValidException)
     * @param headers Cabeceras de la petición
     * @param status  Estado de la petición
     * @param request Petición web
     * @return ResponseEntity con los detalles de la excepción
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();

            String msg = error.getDefaultMessage();

            errors.put(fieldName, msg);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    /**
     * Método que maneja las excepciones generales de la app(Exception)
     * @param exception  Excepción lanzada por la aplicación
     * @param webRequest Petición web
     * @return ResponseEntity con los detalles de la excepción
     * @see GestBandaException Clase que define el mensaje de la excepción
     * @see ErrorDetailed Clase que contiene los detalles de la excepción.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailed> handleGeneralException(
            Exception exception, WebRequest webRequest) {
        ErrorDetailed errorDetails =
                new ErrorDetailed(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime(), exception.getMessage(),
                        ErrorCodes.GENERIC_ERROR.getCode());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
