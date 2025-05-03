package no.ntnu.idata2900.group3.chairspace.security;

import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.server.ResponseStatusException;

/**
 * Security configuration class for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Value("${frontend.url}")
	private String frontendUrl;

	@Value("${DEV_MODE}")
	private boolean devMode;

	UserService userService;


	/**
	 * Creates new instance of security configuration.
	 *
	 * @param userService user service
	 */
	public SecurityConfiguration(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Configures the security filter chain for the application.
	 * If devMode is enabled, all requests are allowed without authentication.
	 * In production, authentication is required for all requests except for the
	 * login and Microsoft login endpoints.
	 *
	 * @param http the HttpSecurity object to configure
	 * @return the configured SecurityFilterChain
	 * @throws Exception if an error occurs during configuration
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// If devMode is enabled, allow all requests without authentication.
		// Should be disabled in production.
		if (devMode) {
			http.csrf(AbstractHttpConfigurer::disable)
				.cors(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
					.requestMatchers("/**").permitAll()
				);
			return http.build();
		}
		http.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth
				// Allow all to access login endpoint
				.requestMatchers("/login").permitAll()
				// Allow all to access microsoft login endpoint
				.requestMatchers("/oauth2/authorization/azure").permitAll()
				// Allow access to documentation endpoints
				.requestMatchers("/api-docs/**").permitAll()
				.requestMatchers("swagger-ui/**").permitAll()
				.requestMatchers("/v3/api-docs/**").permitAll()
				.anyRequest().authenticated()
			)
			// A flaw of this logic is that until the user is logged in,
			// All errors will return 401. So if debugging, check the logs regularly.
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(
					new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
				)
			)
			.oauth2Login(oauth2 -> oauth2
				// .loginPage() Forces the user to user microsoft login.
				// If the application is extended to support other login methods
				// this should be changed.
				.loginPage("/oauth2/authorization/azure")
				.defaultSuccessUrl(frontendUrl, true)
				.userInfoEndpoint(userInfo -> userInfo
					.oidcUserService(oidcUserService()
				)
			)
			);
		return http.build();
	}

	private OAuth2UserService<OidcUserRequest, OidcUser>  oidcUserService() {
		OidcUserService delegate = new OidcUserService();

		return request -> {
			OidcUser oidcUser = delegate.loadUser(request);
			try {
				userService.syncUser(oidcUser);
			} catch (InvalidArgumentCheckedException e) {
				throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to synchronize user. OidcUser lacks data to create user entity: "
					+ e.getMessage(),
					e
				);
			}
			return oidcUser;
		};
	}
}
