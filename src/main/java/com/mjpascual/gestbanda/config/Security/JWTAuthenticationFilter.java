package com.mjpascual.gestbanda.config.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.ErrorDetailed;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.services.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.TimeZone;

/**
 * Filter that will authorize the user to access the endpoints
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UsersService usersService;
    private final TokenUtils tokenUtils;

    public JWTAuthenticationFilter(UsersService usersService, TokenUtils tokenUtils) {
        this.usersService = usersService;
        this.tokenUtils = tokenUtils;
    }

    /**
     * Method that will create a UsernamePasswordAuthenticationToken and use it to verify if the user matches the database
     * @param request HTTP request
     * @param response HTTP response
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        AuthCredentials authCredentials = new AuthCredentials();

        try {
            authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class);

        } catch (IOException e) {
            ErrorDetailed errorDetails =
                    new ErrorDetailed(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime(), "Error al leer los datos de inicio de sesión",
                            ErrorCodes.READ_ERROR.getCode());

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            ObjectMapper mapper = new ObjectMapper();
            try {
                response.getWriter().write(mapper.writeValueAsString(errorDetails));
                response.getWriter().flush();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            return null;
        }

        UsernamePasswordAuthenticationToken usernamePAT = new UsernamePasswordAuthenticationToken(authCredentials.getEmail().toLowerCase(), authCredentials.getPassword(), Collections.emptyList());

        return getAuthenticationManager().authenticate(usernamePAT);
    }

    /**
     * Method triggered if authentication is successful. This method will add the user's token via headers.
     * It will also send user information in the body.
     * @param request HTTP request
     * @param response HTTP response
     * @param chain Filter chain
     * @param authResult Authentication result
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        String token = tokenUtils.createToken(userDetails.getUsername());

        UsersResponse user = usersService.getLoginInfo(userDetails.getUsername());

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(user));


        response.getWriter().flush();


        super.successfulAuthentication(request, response, chain, authResult);
    }

    /**
     * Method triggered if authentication fails. This method will return an error message.
     * @param request HTTP request
     * @param response HTTP response
     * @param failed
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ErrorDetailed errorDetails =
                new ErrorDetailed(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime(), "Las credenciales no son válidas",
                        ErrorCodes.USER_NOT_FOUND.getCode());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorDetails));


        response.getWriter().flush();

    }
}
