package com.mjpascual.gestbanda.interfaces;

/**
 * Interfaz que obligará a los DTO a tener un trimFields y un valid
 */
public interface IValidator {

    /**
     * Método que comprobará si los valores obligatorios no son null.
     */
    void verifyNulls();

    /**
     * Método que quitará los espacios al principio y al final de los atributos
     */
    void trimFields();

    /**
     * Método que validará los distintos atributos del DTO, en caso de que alguno no sea válido lanzará una excepcion
     */
    void validateFields();

    /**
     * Método que llamaremos desde el controlador y será el engargado de llamar a trimFields() y a validateFields()
     */
    void validate();
}
