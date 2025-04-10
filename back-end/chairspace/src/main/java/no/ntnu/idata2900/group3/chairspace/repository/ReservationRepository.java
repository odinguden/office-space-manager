package no.ntnu.idata2900.group3.chairspace.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Reservation entity.
 */
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, UUID> {
	/**
	 * Returns a list of all reservations in an area that fall within the given timespace. This
	 * includes reservations that are only partially within the timespace, as well as reservations
	 * that start before the timespace and end after it.
	 *
	 * @param areaId the id of the area to get from
	 * @param startTime the time to start search from
	 * @param endTime the time to end search from
	 * @return a list of all reservations for the given area that fall between start and end
	 */
	@Query("""
		SELECT res
		FROM Reservation res
		WHERE res.area.id = ?1
		AND (
			res.startDateTime BETWEEN ?2 AND ?3
			OR
			res.endDateTime BETWEEN ?2 and ?3
			OR
			(
				res.startDateTime < ?2 AND res.endDateTime > ?3
			)
		)
		""")
	public List<Reservation> findReservationsForAreaInTimePeriod(
		UUID areaId,
		LocalDateTime startTime,
		LocalDateTime endTime
	);

	/**
	 * Checks if the time slot is occupied by an existing reservation.
	 *
	 * @param areaId the area to check for
	 * @param startTime the start time of the time slot
	 * @param endTime the end time of the time slot
	 * @return true if the time slot is free
	 */
	@Query("""
		SELECT COUNT(res) = 0
		FROM Reservation res
		WHERE res.area.id = ?1
		AND (
			res.startDateTime BETWEEN ?2 AND ?3
			OR
			res.endDateTime BETWEEN ?2 and ?3
			OR
			(
				res.startDateTime < ?2 AND res.endDateTime > ?3
			)
		)
		""")
	public boolean isTimeSlotFree(UUID areaId, LocalDateTime startTime, LocalDateTime endTime);
}