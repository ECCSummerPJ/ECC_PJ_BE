package com.linkrap.BE.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long accessMs;
    private final long refreshMs;

    public JwtTokenProvider(
            @Value("${jwt.secret:change-me-change-me-change-me-change-me}") String secret,
            @Value("${jwt.access-ms:1800000}") long accessMs,     // 30분
            @Value("${jwt.refresh-ms:1209600000}") long refreshMs // 14일
    ) {
        // secret 길이는 256bit 이상 권장
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessMs = accessMs;
        this.refreshMs = refreshMs;
    }

    public String generateAccessToken(Integer userId, String loginId) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(loginId)
                .claim("userId", userId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Integer userId) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject("refresh")
                .claim("userId", userId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
