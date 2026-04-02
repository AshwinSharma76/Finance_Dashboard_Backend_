package com.example.finance_dashboard_backend.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey123"; // 32+ chars required for HS256
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    /**
     * Convert secret key string to Key object for signing
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Generate JWT token for a given username
     */
    public String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        logger.info("Generated JWT token for user: {}", username);
        return token;
    }

    /**
     * Extract username from JWT token
     */
    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            logger.warn("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Validate token for the given username
     */
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        boolean valid = extractedUsername != null && extractedUsername.equals(username) && !isTokenExpired(token);
        if (!valid) {
            logger.warn("Token validation failed for user: {}", username);
        }
        return valid;
    }

    /**
     * Check if token is expired
     */
    private boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            logger.warn("Failed to check token expiration: {}", e.getMessage());
            return true; // treat as expired if parsing fails
        }
    }
}