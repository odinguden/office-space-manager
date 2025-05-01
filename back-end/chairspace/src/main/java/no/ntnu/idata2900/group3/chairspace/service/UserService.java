package no.ntnu.idata2900.group3.chairspace.service;

import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
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
	 * Returns all areas the user administrates in list.
	 *
	 * @param id the id of the user
	 * @return list containing the areas the user administrates
	 * @throws ElementNotFoundException if a user with matching id cannot be found
	 */
	public List<Area> getUserAreas(UUID id) throws ElementNotFoundException {
		if (!userRepository.existsById(id)) {
			throw ElementNotFoundException.USER_NOT_FOUND;
		}
		return userRepository.getUserAreas(id);
	}

	/**
	 * Gets all reservations created by the user.
	 *
	 * @param id the id of the user
	 * @return the reservations the user have made in a list
	 * @throws ElementNotFoundException if a user with matching id cannot be found
	 */
	public List<Reservation> getUserReservations(UUID id) throws ElementNotFoundException {
		if (!userRepository.existsById(id)) {
			throw ElementNotFoundException.USER_NOT_FOUND;
		}
		return userRepository.getUserReservations(id);
	}

	/**
	 * Synchronizes the OIDC user with the database.
	 * If the user does not exist in the database, it will be created.
	 * If the user already exists, the information will be updated.
	 *
	 * @param user the OIDC user to synchronize
	 * @return the user object from the database
	 */
	public User synchUser(OidcUser user) {
		String externalId = user.getSubject();
		User existingUser = userRepository.findByExternalId(externalId);
		if (existingUser != null) {
			return existingUser;
		}
		User newUser = null;
		try {
			//TODO: error handling
			newUser = new User(user.getFullName(), user.getEmail(), externalId);
		} catch (InvalidArgumentCheckedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (userRepository.count() == 0) {
			newUser.setAdmin(true);
		}
		userRepository.save(newUser);
		return newUser;

	}

	public User getSessionUser() {
		User user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof OidcUser) {
			OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
			user = userRepository.findByExternalId(oidcUser.getSubject());
		}
		return user;
	}

}
