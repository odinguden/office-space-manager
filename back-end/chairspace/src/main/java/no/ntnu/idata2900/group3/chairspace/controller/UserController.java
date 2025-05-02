package no.ntnu.idata2900.group3.chairspace.controller;

import java.util.Set;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleArea;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.service.AreaService;
import no.ntnu.idata2900.group3.chairspace.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;



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
	private UserService userService;
	//TODO: remove this if possible
	private AreaService areaService;

	/**
	 * Creates a new user controller.
	 *
	 * @param userService autowired user service.
	 * @param areaService autowired area service.
	 */
	public UserController(UserService userService, AreaService areaService) {
		super(userService);
		this.userService = userService;
	}

	/**
	 * Sets the user as admin or not admin.
	 * Can only be performed by an admin user.
	 *
	 * @param userId the id of the user to set as admin or not admin
	 * @param adminState true if the user should be set as admin, false if not
	 * @return the response entity with status OK if successful
	 * @throws ResponseStatusException if the user is not found or the current user is not an admin
	 */
	@PostMapping("{userId}/admin/{adminState}")
	public ResponseEntity<String> setAdmin(
		@PathVariable UUID userId, @PathVariable boolean adminState
	) {
		User user = userService.get(userId);
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found");
		}
		User currentUser = userService.getSessionUser();
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not logged in");
		}
		if (!currentUser.isAdmin()) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "User is not authorized to perform this action"
			);
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

	/**
	 * Adds an area to the user's favorites.
	 * This can only be performed by a logged-in user.
	 *
	 * @param areaId the id of the area to add to favorites
	 * @return the response entity with status 200 OK if successful
	 * @throws ResponseStatusException 401 if the user is not logged in
	 * @throws ResponseStatusException 404 if the area is not found
	 */
	@PostMapping("/favorite/{areaId}")
	public ResponseEntity<String> addFavorite(@PathVariable UUID areaId) {
		Area area = areaService.get(areaId);
		if (area == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area was not found");
		}
		try {
			userService.addFavorite(area);
		} catch (IllegalStateException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not logged in");
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Removes an area from the user's favorites.
	 *
	 * @param areaId the id of the area to remove from favorites
	 * @return the response entity with status 200 OK if successful
	 * @throws ResponseStatusException 401 if the user is not logged in
	 * @throws ResponseStatusException 404 if the area is not found
	 */
	@DeleteMapping("/favorite/{areaId}")
	public ResponseEntity<String> removeFavorite(@PathVariable UUID areaId) {
		Area area = areaService.get(areaId);
		if (area == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area was not found");
		}
		try {
			userService.removeFavorite(area);
		} catch (IllegalStateException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not logged in");
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Gets the user's favorites.
	 * This can only be performed by a logged-in user.
	 *
	 * @return the response entity with the user's favorites
	 * @throws ResponseStatusException 401 if the user is not logged in
	 */
	@GetMapping("/favorite")
	public ResponseEntity<Set<SimpleArea>> getFavorites() {
		Set<SimpleArea> favorites;
		try {
			favorites = userService.getFavorites();
		} catch (IllegalStateException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not logged in");
		}
		return new ResponseEntity<>(favorites, HttpStatus.OK);
	}
}
