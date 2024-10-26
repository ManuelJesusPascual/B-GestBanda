package com.mjpascual.gestbanda.interfaces;

/**
 * Interface that enforces DTOs to have trimFields and validate methods.
 */
public interface IValidator {

    /**
     * Method that checks if required values are not null.
     */
    default void verifyNulls() {

    }

    /**
     * Method that removes leading and trailing spaces from the attributes.
     */
    default void trimFields() {

    }

    /**
     * Method that validates the different attributes of the DTO; if any are invalid, it will throw an exception.
     */
    default void validateFields() {

    }

    /**
     * Method that will be called from the controller and will be responsible for calling trimFields() and validateFields().
     */
    default void validate() {

    }
}
