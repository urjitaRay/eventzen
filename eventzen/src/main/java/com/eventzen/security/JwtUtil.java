package com.eventzen.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import com.eventzen.model.User;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // ✅ Must be at least 32 characters
    private final String SECRET = "mysecretkeymysecretkeymysecretkey";

    // 🔑 Signing Key
    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // ✅ GENERATE TOKEN (7 days expiration)
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // 7 days
                .signWith(getKey())
                .compact();
    }

    // ✅ EXTRACT EMAIL (SUBJECT)
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ✅ EXTRACT EXPIRATION
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ✅ GENERIC CLAIM EXTRACTOR
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    // ✅ GET ALL CLAIMS
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ CHECK IF TOKEN EXPIRED
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ✅ VALIDATE TOKEN (CRITICAL FOR AUTH)
    public boolean validateToken(String token, User user) {
        final String email = extractEmail(token);
        return (email.equals(user.getEmail()) && !isTokenExpired(token));
    }
}