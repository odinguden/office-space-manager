package no.ntnu.idata2900.group3.chairspace.controllers;

import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//TODO Swagger documentation

/**
 * Controller for user entity.
 */
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
	@GetMapping("")
	public ResponseEntity<Iterable<User>> getMethodName() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}

	/**
	 * Method to add a new user to the database.
	 *
	 * @param user user to add to repository
	 * @return 400 bad request if user is null, 201 created if entity was created without issues.
	 */
	@PostMapping("")
	public ResponseEntity<String> newUser(@RequestBody User user) {
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		repository.save(user);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}
