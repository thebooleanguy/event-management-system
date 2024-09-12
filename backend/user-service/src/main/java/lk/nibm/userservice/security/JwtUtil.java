package lk.nibm.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtUtil is a utility class for handling JWT (JSON Web Token) operations such as token generation,
 * extraction of claims, and validation. It provides methods to create and parse JWTs using a secret key
 * and to validate tokens against user details.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret; // The secret key for signing JWTs

    @Value("${jwt.expiration}")
    private Long expiration; // The expiration time of JWTs in seconds

    /**
     * Constructs a JwtUtil instance with secret and expiration values injected from application properties.
     */
    public JwtUtil() {
    }

    /**
     * Retrieves the signing key for JWT operations by decoding the base64-encoded secret.
     *
     * @return The signing key for JWT.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret); // Decode the base64 key
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date extracted from the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token using a claims resolver function.
     *
     * @param token The JWT token.
     * @param claimsResolver A function to extract a specific claim from the Claims object.
     * @param <T> The type of the claim to extract.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token The JWT token.
     * @return The Claims object containing all claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the JWT token is expired based on its expiration date.
     *
     * @param token The JWT token.
     * @return True if the token is expired, otherwise false.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT token for a given user.
     *
     * @param userDetails The user details to include in the token.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Creates a JWT token with specified claims and subject (username).
     *
     * @param claims The claims to include in the token.
     * @param subject The subject (username) for the token.
     * @return The created JWT token.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Validates a JWT token by checking if the username matches and the token is not expired.
     *
     * @param token The JWT token.
     * @param userDetails The user details to validate against.
     * @return True if the token is valid, otherwise false.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
