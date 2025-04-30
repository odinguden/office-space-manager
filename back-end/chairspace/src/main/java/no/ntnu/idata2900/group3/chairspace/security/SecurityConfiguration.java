package no.ntnu.idata2900.group3.chairspace.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * Security configuration class for the application.
 */
@Configuration
public class SecurityConfiguration {

	/**
	 * Security and sutch.
	 *
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login").permitAll() // Allow all to access login endpoint
				.requestMatchers("/api-docs/**").permitAll() // TODO Remove later
				.requestMatchers("swagger-ui/**").permitAll() // TODO Remove later
				.requestMatchers("/v3/api-docs/**").permitAll() // TODO Remove later
				.anyRequest().authenticated()
			)
			.exceptionHandling(e -> e
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // Return 401 for unauthorized requests
			);
			return http.build();
	}
}
