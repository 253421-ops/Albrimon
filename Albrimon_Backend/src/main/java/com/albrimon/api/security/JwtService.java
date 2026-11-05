package com.albrimon.api.security;

import com.albrimon.api.user.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;
    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32)
            throw new IllegalArgumentException("JWT_SECRET debe tener al menos 32 caracteres.");
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }
    public String generate(UserEntity user) {
        Date now = new Date();
        return Jwts.builder().subject(user.getId().toString())
            .claim("role", user.getRole().name()).issuedAt(now)
            .expiration(new Date(now.getTime() + expirationMs)).signWith(key).compact();
    }
    public String subject(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            .getPayload().getSubject();
    }
}
