package no.ntnu.idata2900.group3.chairspace.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * Controller to handle authentication-related requests.
 */
@RestController
public class AuthController {

	/**
	 * Redirects to the Azure login page.
	 *
	 * @param response the HTTP response
	 * @throws IOException if an I/O error occurs
	 */
	@GetMapping("/login")
	@Operation(
		summary = "Redirects the user to microsoft login"
	)
	public void login(HttpServletResponse response) throws IOException {
		response.sendRedirect("/oauth2/authorization/azure");
	}
}
