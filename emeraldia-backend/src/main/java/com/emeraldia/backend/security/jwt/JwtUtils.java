package com.emeraldia.backend.security.jwt;

import com.emeraldia.backend.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${jwtsecret}")
  private String jwtSecret;

  @Value("${jwtExpirationMs}")
  private int jwtExpirationMs;

  /**
   * Generate a JWT token based on the provided authentication.
   *
   * @param authentication The authentication object containing user details.
   * @return The generated JWT token as a string.
   */
  public String generateJwtToken(Authentication authentication) {
    // Get the user details from the authentication object
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    // Extraer roles como strings
    List<String> roles = userPrincipal.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    // Build and return the JWT token
    return Jwts.builder()
            .setSubject((userPrincipal.getUsername())) // Set the subject (username)
            .claim("roles", roles)
            .setIssuedAt(new Date()) // Set the issue date
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Set the expiration date
            .signWith(key(), SignatureAlgorithm.HS256)
            // Sign the token using the secret key and algorithm
            .compact(); // Compact the JWT into a string
  }

  /**
   * Create a signing key from the JWT secret.
   *
   * @return The signing key as a Key object.
   */
  private Key key(){
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  /**
   * Extract the username from the given JWT token.
   *
   * @param token The JWT token.
   * @return The username extracted from the token.
   */
  public String getUserNameFromJwtToken(String token) {
    // Parse the JWT token and return the subject (username)
    return Jwts.parserBuilder().setSigningKey(key()).build()
            .parseClaimsJws(token).getBody().getSubject();
  }

  /**
   * Validate the given JWT token.
   *
   * @param authToken The JWT token to validate.
   * @return True if the token is valid, false otherwise.
   */
  public boolean validateJwtToken(String authToken) {
    try {
      // Parse the token and verify its signature
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true; // Token is valid
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage()); // Log invalid token error
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage()); // Log expired token error
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage()); // Log unsupported token error
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage()); // Log empty claims error
    }

    return false; // Token is invalid
  }
}
