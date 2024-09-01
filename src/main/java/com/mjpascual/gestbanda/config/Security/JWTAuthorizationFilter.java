package com.mjpascual.gestbanda.config.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasText;

/**
 * Filtro que verificará si el token que envía el usuario es válido o no. Si es válido obtendrá la autenticación de este.
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String Bearertoken = getTokenFromRequest(request);

        if (!isNull(Bearertoken)) {

            String token = Bearertoken.replace("Bearer ", "");
            UsernamePasswordAuthenticationToken usernamePAT = TokenUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(usernamePAT);

        }
        filterChain.doFilter(request, response);
    }

    /**
     * Método que obtendrá el token de la petición
     * @param request Petición HTTP
     * @return Si existe un token lo devolverá, en caso de no existir, devolverá null.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

}
