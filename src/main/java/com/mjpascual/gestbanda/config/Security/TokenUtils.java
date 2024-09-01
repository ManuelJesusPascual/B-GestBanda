package com.mjpascual.gestbanda.config.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.crypto.SecretKey;
import java.util.*;

public class TokenUtils {

    private final static String ACCESS_TOKEN_SECRET = "***REMOVED***";
    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 2_592_000L;

    /**
     * Método que usaremos para crear un nuevo token
     * @param email Email del usuario
     * @return
     */
    public static String createToken(String email) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1_000;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(System.currentTimeMillis() + expirationTime);
        Date expirationDate = calendar.getTime();

        Map<String, Object> extra = new HashMap<>();
        extra.put("email", email);

        return Jwts.builder().subject(email).expiration(expirationDate).claims(extra).signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes())).compact();
    }

    /**
     * Método que usaremos para obtener la autenticación del usuario a partir de su token.
     * @param token Token del usuario
     * @return UsernamePasswordAuthenticationToken con el email del usuario.
     */
    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes());
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            String email = claims.getSubject();
            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (JwtException e) {
            return null;
        }
    }
}
