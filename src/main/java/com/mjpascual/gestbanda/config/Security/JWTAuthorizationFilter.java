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
 * Filter that will verify if the token sent by the user is valid or not. If valid, it will retrieve the authentication.
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;

    private static final String BEARER_PREFIX = "Bearer ";

    public JWTAuthorizationFilter(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenFromRequest(request);

        if (!isNull(token)) {
            UsernamePasswordAuthenticationToken usernamePAT = tokenUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(usernamePAT);

        }
        filterChain.doFilter(request, response);
    }

    /**
     * Method that will retrieve the token from the request. Accepts both "Bearer &lt;token&gt;"
     * and the raw token, which is what the mobile app sends.
     * @param request HTTP request
     * @return If a token exists, it will return it. If not, it will return null.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!hasText(authorization)) {
            return null;
        }
        String value = authorization.trim();
        return value.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())
                ? value.substring(BEARER_PREFIX.length()).trim()
                : value;
    }

}
