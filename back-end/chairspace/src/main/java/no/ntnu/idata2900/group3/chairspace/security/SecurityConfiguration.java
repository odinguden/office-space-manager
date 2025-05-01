package no.ntnu.idata2900.group3.chairspace.security;

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

/**
 * Security configuration class for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Value("${frontend.url}")
	private String frontendUrl;

	UserService userService;


	/**
	 * Creates new instance of security configuration.
	 *
	 * @param userService user service
	 */
	public SecurityConfiguration(UserService userService) {
		this.userService = userService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth
				// Allow all to access login endpoint
				.requestMatchers("/login").permitAll()
				// Allow all to access microsoft login endpoint
				.requestMatchers("/oauth2/authorization/azure").permitAll()
				// TODO Remove later
				.requestMatchers("/api-docs/**").permitAll()
				// TODO Remove later
				.requestMatchers("swagger-ui/**").permitAll()
				// TODO Remove later
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
			userService.synchUser(oidcUser);
			return oidcUser;
		};
	}
}
