package no.ntnu.idata2900.group3.chairspace.service;

import java.util.Set;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.ElementNotFoundException;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with and managing {@link User}s.
 *
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@Service
public class UserService extends EntityService<User, UUID> {
	private UserRepository userRepository;

	/**
	 * Creates a new user service.
	 *
	 * @param repository autowired UserRepository
	 */
	public UserService(UserRepository repository) {
		super(repository);
		this.userRepository = repository;
	}

	/**
	 * Sets the user as admin or not admin.
	 *
	 * @param userId the user to set as admin or not admin
	 * @param isAdmin true if the user should be set as admin
	 * @throws ElementNotFoundException if the user does not exist in the database
	 */
	public void setAdmin(UUID userId, boolean isAdmin) throws ElementNotFoundException {
		if (userId == null) {
			throw new IllegalArgumentException("User was null when value was expected");
		}
		User user = get(userId);
		if (user == null) {
			throw ElementNotFoundException.USER_NOT_FOUND;
		}
		user.setAdmin(isAdmin);
		userRepository.save(user);
	}

	/**
	 * Synchronizes the OIDC user with the database.
	 * If the user does not exist in the database, it will be created.
	 * If the user already exists, the information will be updated.
	 *
	 * @param user the OIDC user to synchronize
	 * @return the user object from the database
	 * @throws InvalidArgumentCheckedException not able to create user entity from OIDC user
	 */
	public User syncUser(OidcUser user) throws InvalidArgumentCheckedException {
		String externalId = user.getSubject();
		User existingUser = userRepository.findByExternalId(externalId);
		if (existingUser != null) {
			return existingUser;
		}
		User newUser = new User(user.getFullName(), user.getEmail(), externalId);
		if (userRepository.count() == 0) {
			newUser.setAdmin(true);
		}
		userRepository.save(newUser);
		return newUser;

	}

	/**
	 * Gets the currently logged in user.
	 *
	 * @return the currently logged in user, or null if no user is logged in
	 */
	public User getSessionUser() {
		User user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof OidcUser) {
			OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
			user = userRepository.findByExternalId(oidcUser.getSubject());
		}
		return user;
	}

	/**
	 * Adds an area to the user's favorites.
	 * If the user is not logged in, an exception is thrown.
	 *
	 * @param area the area to add to favorites
	 * @throws IllegalStateException if the user is not logged in
	 */
	public void addFavorite(Area area) {
		User user = getSessionUser();
		if (user == null) {
			throw new IllegalStateException("User is not logged in");
		}
		if (area == null) {
			throw new IllegalArgumentException("Area was null when value was expected");
		}
		user.addFavoriteArea(area);
		userRepository.save(user);
	}

	/**
	 * Removes an area from the user's favorites.
	 * If the user is not logged in, an exception is thrown.
	 *
	 * @param area the area to remove from favorites
	 * @throws IllegalStateException if the user is not logged in
	 */
	public void removeFavorite(Area area) {
		User user = getSessionUser();
		if (user == null) {
			throw new IllegalStateException("User is not logged in");
		}
		user.removeFavoriteArea(area);
		userRepository.save(user);
	}

	/**
	 * Gets all the favorites of the user.
	 *
	 * @return a list of areas the user has marked as favorites
	 * @throws IllegalStateException if the user is not logged in
	 */
	public Set<Area> getFavorites() {
		User user = getSessionUser();
		if (user == null) {
			throw new IllegalStateException("User is not logged in");
		}
		return user.getFavoriteAreas();
	}
}
