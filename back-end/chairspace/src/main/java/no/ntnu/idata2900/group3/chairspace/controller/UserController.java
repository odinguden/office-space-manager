package no.ntnu.idata2900.group3.chairspace.controller;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import no.ntnu.idata2900.group3.chairspace.assembler.AreaAssembler;
import no.ntnu.idata2900.group3.chairspace.assembler.UserAssembler;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleArea;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleUser;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.ElementNotFoundException;
import no.ntnu.idata2900.group3.chairspace.service.AreaService;
import no.ntnu.idata2900.group3.chairspace.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	private final UserService userService;
	private final AreaService areaService;
	private final UserAssembler userAssembler;
	private final AreaAssembler areaAssembler;

	/**
	 * Creates a new user controller.
	 *
	 * @param userService autowired user service.
	 * @param areaService autowired area service.
	 * @param userAssembler autowired user assembler
	 * @param areaAssembler autowired area assembler.
	 */
	public UserController(
		UserService userService,
		AreaService areaService,
		UserAssembler userAssembler,
		AreaAssembler areaAssembler
	) {
		super(userService);
		this.userService = userService;
		this.areaService = areaService;
		this.userAssembler = userAssembler;
		this.areaAssembler = areaAssembler;
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
	@PostMapping("{userId}/admin/")
	public ResponseEntity<String> setAdmin(
		@PathVariable UUID userId, @RequestParam boolean adminState
	) {
		hasPermissionToPut();
		User currentUser = userService.getSessionUser();
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not logged in");
		}
		if (!currentUser.isAdmin()) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "User is not authorized to perform this action"
			);
		}
		try {
			userService.setAdmin(userId, adminState);
		} catch (ElementNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

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
		super.hasPermissionToPost();
		Area area = areaService.get(areaId);
		if (area == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area was not found");
		}
		try {
			userService.addFavorite(area);
		} catch (IllegalStateException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not logged in");
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
		super.hasPermissionToDelete();
		Area area = areaService.get(areaId);
		if (area == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area was not found");
		}
		try {
			userService.removeFavorite(area);
		} catch (IllegalStateException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not logged in");
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
		super.hasPermissionToGet();
		Set<Area> areas;
		try {
			areas = userService.getFavorites();
		} catch (IllegalStateException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not logged in");
		}
		Set<SimpleArea> favorites = areas.stream()
			.map(areaAssembler::toSimpleArea)
			.collect(Collectors.toSet());
		return new ResponseEntity<>(favorites, HttpStatus.OK);
	}

	/**
	 * Checks if the given area is a favorite of the user.
	 * This can only be performed by a logged-in user.
	 *
	 * @param areaId the id of the area to check
	 * @return the response entity with true if the area is a favorite, false otherwise
	 * @throws ResponseStatusException 401 if the user is not logged in
	 * @throws ResponseStatusException 404 if the area is not found
	 */
	@GetMapping("/favorite/{areaId}")
	public ResponseEntity<Boolean> isFavorite(@PathVariable UUID areaId) {
		super.hasPermissionToGet();
		boolean isFavorite;
		Area area = areaService.get(areaId);
		if (area == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area was not found");
		}
		try {
			isFavorite = userService.isFavorite(area);
		} catch (IllegalStateException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not logged in");
		}
		return new ResponseEntity<>(isFavorite, HttpStatus.OK);
	}

	/**
	 * Gets the current logged in user, or throws 401 if there is no currently logged in user.
	 *
	 * @return 200 OK with the user if requester is logged in, 401 otherwise
	 */
	@GetMapping("/whoami")
	public ResponseEntity<SimpleUser> whoAmI() {
		User sessionUser = userService.getSessionUser();
		if (sessionUser == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(
			userAssembler.toSimple(sessionUser),
			HttpStatus.OK
		);
	}
}
