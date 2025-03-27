package no.ntnu.idata2900.group3.chairspace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;



/**
 * Controller for user entity.
 */
//TODO: When this class is updated for authentication, there will need to be better feedback when deleting users that administer areas or reservations
@CrossOrigin(origins = "$frontend.url")
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController<User, UUID> {
	@Autowired
	private UserRepository userRepository;

	protected UserController(UserRepository userRepository) {
		super(userRepository);
	}

	/**
	 * Returns the areas a user administers.
	 *
	 * @param id UUID of user
	 * @return a response entity containing the areas this user administers with the code 200 OK
	 */
	@Operation(
		summary = "Gets the areas a user administers",
		description = "Finds the areas this user has admin privileges in"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found the user, and returned the areas the user administers"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Could not find a user with the given Id"
			)
	})
	@GetMapping("/{id}/areas")
	public ResponseEntity<List<Area>> getUserAreas(@PathVariable UUID id) {
		// TODO: authenticate user
		// TODO: Return DTO for area instead of the entire area class
		Optional<User> optionalUser = userRepository.findById(id);

		if (!optionalUser.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		List<Area> areas = userRepository.getUserAreas(id);

		return new ResponseEntity<>(areas, HttpStatus.OK);
	}

	/**
	 * Returns the reservations made by this user.
	 *
	 * @param id UUID of user
	 * @return a response entity containing a list of the reservations made by
	 *     this user administers with the code 200 OK
	 */
	@Operation(
		summary = "Gets the reservations the user has made",
		description = "Finds the reservations the user has made"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found the user, and returned the reservations the user has made"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Could not find a user with the given Id"
			)
	})
	@GetMapping("/{id}/reservations")
	public ResponseEntity<List<Reservation>> getUserReservations(@PathVariable UUID id) {
		// TODO: authenticate user
		Optional<User> optionalUser = userRepository.findById(id);

		if (!optionalUser.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		List<Reservation> reservations = userRepository.getUserReservations(id);
		return new ResponseEntity<>(reservations, HttpStatus.OK);
	}
	
}
