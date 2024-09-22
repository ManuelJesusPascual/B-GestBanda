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
     * Method to create a new token.
     * @param email User's email
     * @return The generated token as a String.
     */
    public static String createToken(String email) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1_000;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(System.currentTimeMillis() + expirationTime);
        Date expirationDate = calendar.getTime();

        Map<String, Object> extra = new HashMap<>();
        extra.put("email", email);

        return Jwts.builder()
                .subject(email)
                .expiration(expirationDate)
                .claims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

    /**
     * Method to retrieve the user's authentication from their token.
     * @param token User's token
     * @return UsernamePasswordAuthenticationToken with the user's email.
     */
    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes());
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String email = claims.getSubject();
            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (JwtException e) {
            return null;
        }
    }
}
