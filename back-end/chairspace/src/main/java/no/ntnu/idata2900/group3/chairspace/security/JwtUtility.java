package no.ntnu.idata2900.group3.chairspace.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utility class for generating and validating JWT tokens.
 */
public class JwtUtility {
	@Value("")
	private String jwtSecret;

	@Value("")
	private long jwtExpiration;

	/**
	 * Generates a JWT token for the given user details.
	 *
	 * @param userDetails the user details for which to generate the token
	 * @return the generated JWT token
	 */
	public String generateToken(UserDetails userDetails) {
		final long currentTimeMillis = System.currentTimeMillis();
		final long expirationTimeMillis = currentTimeMillis + jwtExpiration * 1000;
		return Jwts.builder()
			.subject(userDetails.getUsername())
			.issuedAt(new Date(currentTimeMillis))
			.expiration(new Date(expirationTimeMillis))
			.signWith(getSigningKey())
			.compact();

	}

	/**
	 * TODO: docs and understanding
	 * @return
	 */
	private SecretKey getSigningKey() {
		byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
		return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
	}

	/* ----- Validation ----- */

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (
			userDetails != null
			&& username.equals(userDetails.getUsername())
			&& !isTokenExpired(token)
			);
	}

	/**
	 * Checks if the give token is valid for the current time.
	 *
	 * @param token the token to check
	 * @return true if the token is expired
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/* ----- Extract claims ----- */

	private String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private <TypeT> TypeT extractClaim(String token, Function<Claims, TypeT> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
			.verifyWith(getSigningKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	public UUID extractUserId(String jwt) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'extractUserId'");
	}
}
