package no.ntnu.idata2900.group3.chairspace.security;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.ntnu.idata2900.group3.chairspace.service.UserService;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter for processing JWT tokens in incoming requests.
 */
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtility jwtUtility;
	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, java.io.IOException {
		String jwt = extractToken(request);
		UUID userId = jwt != null ? jwtUtility.extractUserId(jwt) : null;
		filterChain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		String token = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
		}
		return null;
	}

	/**
	 * Strips the "Bearer " prefix from the authorization header.
	 *
	 * @param authHeader the authorization header containing the JWT token
	 * @return JWT token without the "Bearer " prefix
	 */
	private String stripBearerPrefix(String authHeader) {
		final int bearerPrefixLength = "Bearer ".length();
		return authHeader.substring(bearerPrefixLength);
	}

	private static boolean notAuthenticatedYet() {
		return SecurityContextHolder.getContext().getAuthentication() == null;
	}

	private static void registerUserAsAuthenticated(HttpServletRequest request, UUID userId) {

	}
}
