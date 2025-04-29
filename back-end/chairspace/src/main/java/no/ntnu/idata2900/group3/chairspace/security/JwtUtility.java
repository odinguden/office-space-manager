package no.ntnu.idata2900.group3.chairspace.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtility {
	@Value("${JWT_SECRET}")
	private String jwtSecret;

	@Value("${JWT_EXPIRATION}")
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

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
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

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
