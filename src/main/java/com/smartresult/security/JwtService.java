package com.smartresult.security;

import com.smartresult.model.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final Key key;
  private final long accessTokenMinutes;

  public JwtService(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.accessTokenMinutes:60}") long accessTokenMinutes) {
    // Allow plain text secret too; if it's base64, decoding will still work for many strings.
    byte[] bytes;
    try {
      bytes = Decoders.BASE64.decode(secret);
    } catch (IllegalArgumentException e) {
      bytes = secret.getBytes();
    }
    this.key = Keys.hmacShaKeyFor(bytes.length >= 32 ? bytes : (secret + "00000000000000000000000000000000").getBytes());
    this.accessTokenMinutes = accessTokenMinutes;
  }

  public String generateAccessToken(Long userId, String email, Role role) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(accessTokenMinutes * 60);
    return Jwts.builder()
        .subject(email)
        .claims(Map.of(
            "uid", userId,
            "role", role.name()
        ))
        .issuedAt(Date.from(now))
        .expiration(Date.from(exp))
        .signWith(key)
        .compact();
  }

  public JwtPrincipal parseAndValidate(String token) {
    var claims = Jwts.parser()
        .verifyWith((javax.crypto.SecretKey) key)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    Long uid = claims.get("uid", Long.class);
    String roleStr = claims.get("role", String.class);
    String email = claims.getSubject();
    Role role = Role.valueOf(roleStr);
    return new JwtPrincipal(uid, email, role);
  }
}

