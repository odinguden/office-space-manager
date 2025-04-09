package no.ntnu.idata2900.group3.chairspace.service;

import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.ElementNotFoundException;
import no.ntnu.idata2900.group3.chairspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for user entity.
 * Connects application logic to database
 */
@Service
public class UserService extends AbstractEntityService<User, UUID> {
	@Autowired
	UserRepository userRepository;

	/**
	 * Creates new instance of user service.
	 *
	 * @param repository user
	 */
	public UserService(UserRepository repository) {
		super(repository);
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
			throw ElementNotFoundException.userNotFoundException();
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
			throw ElementNotFoundException.userNotFoundException();
		}
		return userRepository.getUserReservations(id);
	}
}
