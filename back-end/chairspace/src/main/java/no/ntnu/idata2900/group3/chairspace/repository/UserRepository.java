package no.ntnu.idata2900.group3.chairspace.repository;

import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Account entity.
 */
@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

	/**
	 * Returns the areas that a single user administers.
	 *
	 * @param id the id of the user
	 * @return a list containing the areas the user administers
	 */
	@Query(value = """
		SELECT area
		FROM Area area
		INNER JOIN area.administrators areaAdmin
		WHERE areaAdmin.id = ?1
		"""
	)
	public List<Area> getUserAreas(UUID id);

	/**
	 * Returns the reservations made by user.
	 *
	 * @param id the id of the user
	 * @return a list containing the reservation made by this user
	 */
	@Query(value = """
		SELECT reservation
		FROM Reservation reservation
		INNER JOIN reservation.user user
		WHERE user.id = ?1
		"""
	)
	public List<Reservation> getUserReservations(UUID id);
}