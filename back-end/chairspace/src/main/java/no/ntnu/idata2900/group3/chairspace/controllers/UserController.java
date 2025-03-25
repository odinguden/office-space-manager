package no.ntnu.idata2900.group3.chairspace.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


//TODO Swagger documentation

/**
 * Controller for user entity.
 */
@CrossOrigin(origins = "$frontend.url")
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController<User, UUID> {

	@Autowired
    private final UserRepository userRepository;
	
	/**
	 * The constructor.
	 *
	 * @param userRepository User repository
	 */
	public UserController(UserRepository userRepository) {
		super(userRepository);
		this.userRepository = userRepository;
	}

	/**
	 * 
	 * TODO: Implement security
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/{id}/administrates")
	public ResponseEntity<List<Area>> getMethodName(@PathVariable UUID id) {
		Optional<User> optionalUser = repository.findById(id);

		if (!optionalUser.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		User user = optionalUser.get();
		List<Area> areas = userRepository.findAreasOfUser(user.getId());
		return new ResponseEntity<>(areas, HttpStatus.I_AM_A_TEAPOT);
	}
}
