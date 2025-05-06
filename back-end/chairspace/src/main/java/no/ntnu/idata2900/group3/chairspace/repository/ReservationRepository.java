package no.ntnu.idata2900.group3.chairspace.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Reservation entity.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
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
		ORDER BY res.startDateTime ASC
		""")
	public List<Reservation> findReservationsForAreaInTimePeriod(
		UUID areaId,
		LocalDateTime startTime,
		LocalDateTime endTime
	);

	/**
	 * Returns a list of all reservations, regardless of area, that fall within the given timespace.
	 * This includes reservations that are only partially within the timespace, as well as
	 * reservations that start before the timespace and end after it.
	 *
	 * @param startTime the time to start search from
	 * @param endTime the time to end search from
	 * @return a list of all reservations for the given area that fall between start and end
	 */
	@Query("""
		SELECT res
		FROM Reservation res
		WHERE res.startDateTime BETWEEN ?1 AND ?2
			OR
			res.endDateTime BETWEEN ?1 and ?2
			OR
			(
				res.startDateTime < ?1 AND res.endDateTime > ?2
			)
		ORDER BY res.area.id, res.startDateTime ASC
		""")
	public List<Reservation> findAllReservationsInTimePeriod(
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

	/**
	 * Returns a list of reservations belonging to the provided user id.
	 *
	 * @param userId the user who's reservations are being fetched
	 * @return a list of reservations belonging to the user
	 */
	public List<Reservation> findAllByUserIdOrderByStartDateTimeAsc(UUID userId);

	/**
	 * Returns a list of reservations belonging to the provided area id.
	 *
	 * @param areaId the area who's reservations are being fetched
	 * @return a list of reservations belonging to the area
	 */
	public List<Reservation> findAllByAreaIdOrderByStartDateTimeAsc(UUID areaId);

	/**
	 * Returns a page of reservations belonging to the provided user id.
	 *
	 * @param userId the user who's reservations are being fetched
	 * @return a page of reservations belonging to the user
	 */
	@Query("""
		SELECT reservation
		FROM Reservation reservation
		WHERE reservation.user.id = ?1
		""")
	public List<Reservation> findAllByUser(UUID userId);
}