package no.ntnu.idata2900.group3.chairspace.controller;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the user feature entity.
 *
 * @see User
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController<User, UUID> {

	/**
	 * Creates a new user controller.
	 *
	 * @param userService autowired user service.
	 */
	public UserController(UserService userService) {
		super(userService);
	}
}
