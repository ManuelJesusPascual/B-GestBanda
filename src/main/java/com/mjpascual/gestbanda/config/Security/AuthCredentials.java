package com.mjpascual.gestbanda.config.Security;

import lombok.Data;

/**
 * Credenciales con las que el usuario se logueará para obtener un token y tener acceso a los endpoints
 */
@Data
public class AuthCredentials {
    private String email;
    private String password;
}
