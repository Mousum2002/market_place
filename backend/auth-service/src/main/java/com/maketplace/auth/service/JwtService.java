package com.maketplace.auth.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private final SecretKey signingKey;
  private final long expirationMs;

  public JwtService(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration-ms}") long expirationMs) {
    this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.expirationMs = expirationMs;
  }

  public String generateToken(String userId, String username) {
    Date now = new Date();

    Date expiry = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .subject(username)
        .claim("userId", userId)
        .issuedAt(now)
        .expiration(expiry)
        .signWith(signingKey, Jwts.SIG.HS256)
        .compact();

  }

  public Key getSigningKey() {
    return signingKey;
  }
}
