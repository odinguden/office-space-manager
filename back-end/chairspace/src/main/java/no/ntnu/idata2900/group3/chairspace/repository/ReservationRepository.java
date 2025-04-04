package no.ntnu.idata2900.group3.chairspace.repository;

import java.util.List;
import java.util.UUID;	
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Reservation entity.
 */
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, UUID> {

	/**
	 * Finds all reservations for a given area.
	 *
	 * @param area the area to find reservations for
	 * @return a list of reservations for the given area
	 */
	@SQL("SELECT * FROM reservation WHERE area = ?1")
	public List<Reservation> findByArea(Area area);
}