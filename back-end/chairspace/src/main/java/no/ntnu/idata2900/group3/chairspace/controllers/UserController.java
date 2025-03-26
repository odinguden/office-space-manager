package no.ntnu.idata2900.group3.chairspace.controllers;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.repository.UserRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user entity.
 */
//TODO: When this class is updated for authentication, there will need to be better feedback when deleting users that administer areas or reservations
@CrossOrigin(origins = "$frontend.url")
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController<User, UUID> {
	protected UserController(UserRepository userRepository) {
		super(userRepository);
	}
}
