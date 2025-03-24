package no.ntnu.idata2900.group3.chairspace.controllers;

import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.repository.UserRepository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


//TODO Swagger documentation

/**
 * Controller for user entity.
 */
@CrossOrigin(origins = "localhost:3000")
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository repository;

	/**
	 * Returns all users in database.
	 *
	 * @return 200 OK if things went good
	 */
	@Operation(
		summary = "Retrieves all active users in the database"
	)
	@GetMapping("")
	public ResponseEntity<Iterable<User>> getAllUsers() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}

	/**
	 * Method to add a new user to the database.
	 *
	 * @param user user to add to repository
	 * @return 400 bad request if user is null, 201 created if entity was created without issues.
	 */
	@Operation(
		summary = "Creates a single user in the database with a uuid"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "User created"
			),
		@ApiResponse(
			responseCode = "400",
			description = "If request body does not fit the expected structure"
			)
		}
	)
	@PostMapping("")
	public ResponseEntity<String> createNewUser(@RequestBody User user) {
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		repository.save(user);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * Updates user
	 * @param id
	 * @param newUser
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody User newUser) {
		//TODO: process PUT request
		return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
		return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}

}
