package com.mjpascual.gestbanda.config.Security;

import lombok.Data;

/**
 * Credentials that the user will use to log in and obtain a token to access the endpoints
 */
@Data
public class AuthCredentials {
    private String email;
    private String password;
}
