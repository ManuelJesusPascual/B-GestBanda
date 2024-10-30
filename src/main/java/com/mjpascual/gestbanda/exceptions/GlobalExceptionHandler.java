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
 * Class that handles exceptions in the application
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Method that handles application exceptions (GestBandaException)
     *
     * @param exception  Exception thrown by the application
     * @param webRequest Web request
     * @return ResponseEntity with the details of the exception
     * @see GestBandaException Class that defines the exception message
     * @see ErrorDetailed Class that contains the details of the exception.
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
     * Method that handles method argument validation exceptions
     *
     * @param ex      Exception thrown by the application (throw new MethodArgumentNotValidException)
     * @param headers Request headers
     * @param status  Status of the request
     * @param request Web request
     * @return ResponseEntity with the details of the exception
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
     * Method that handles general application exceptions (Exception)
     *
     * @param exception  Exception thrown by the application
     * @param webRequest Web request
     * @return ResponseEntity with the details of the exception
     * @see GestBandaException Class that defines the exception message
     * @see ErrorDetailed Class that contains the details of the exception.
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
