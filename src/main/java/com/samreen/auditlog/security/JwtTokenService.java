package com.samreen.auditlog.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
@Service public class JwtTokenService {
 private final SecretKey key; private final String issuer,audience; private final Duration expiration;
 public JwtTokenService(@Value("${app.jwt.secret}")String secret,@Value("${app.jwt.issuer}")String issuer,@Value("${app.jwt.audience}")String audience,@Value("${app.jwt.expiration:PT15M}")Duration expiration){if(secret.length()<32)throw new IllegalArgumentException("JWT secret must be at least 32 characters");key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));this.issuer=issuer;this.audience=audience;this.expiration=expiration;}
 public String issue(String subject,List<String> roles){Instant now=Instant.now();return Jwts.builder().subject(subject).issuer(issuer).audience().add(audience).and().claim("roles",roles).issuedAt(Date.from(now)).expiration(Date.from(now.plus(expiration))).signWith(key).compact();}
 public Claims parse(String token){return Jwts.parser().verifyWith(key).requireIssuer(issuer).requireAudience(audience).build().parseSignedClaims(token).getPayload();}
}
