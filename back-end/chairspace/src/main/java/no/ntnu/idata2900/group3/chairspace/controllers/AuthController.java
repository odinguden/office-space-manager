package no.ntnu.idata2900.group3.chairspace.controllers;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class AuthController {
	
	public final LoginUrlAuthenticationEntryPoint loginEntryPoint = new LoginUrlAuthenticationEntryPoint("/oauth2/authorization/azure");

	@GetMapping("/login")
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		loginEntryPoint.commence(request, response, null);
	}
}
