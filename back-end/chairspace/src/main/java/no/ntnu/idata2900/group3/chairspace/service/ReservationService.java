package no.ntnu.idata2900.group3.chairspace.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.repository.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with and managing {@link Reservation}s.
 *
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@Service
public class ReservationService extends EntityService<Reservation, UUID> {
	ReservationRepository reservationRepository;

	/**
	 * Creates a new user service.
	 *
	 * @param repository autowired UserRepository
	 */
	public ReservationService(ReservationRepository repository) {
		super(repository);
		this.reservationRepository = repository;
	}

	@Override
	// Override to ensure reservations cannot overlap
	protected boolean save(Reservation reservation) {
		boolean canAdd = reservationRepository.isTimeSlotFree(
			reservation.getArea().getId(),
			reservation.getStart(),
			reservation.getEnd()
		);

		if (canAdd) {
			canAdd = super.save(reservation);
		}

		return canAdd;
	}

	/**
	 * Gets all reservations belonging to a given user ID.
	 *
	 * @param userId the id of the user to get the reservations of
	 * @return a list of reservation DTOs belonging to the given user id.
	 */
	public List<Reservation> getReservationsForUser(UUID userId) {
		return this.reservationRepository.findAllByUserIdOrderByStartDateTimeAsc(userId);
	}

	/**
	 * Gets all reservations belonging to a given area.
	 *
	 * @param areaId the id to get reservations from
	 * @return all reservations belonging to a given area
	 */
	public List<Reservation> getReservationsForArea(UUID areaId) {
		return this.reservationRepository.findAllByAreaIdOrderByStartDateTimeAsc(areaId);
	}

	/**
	 * Gets all reservations belonging to a given area within the specified time period. This
	 * includes all reservations that occur within the time frame.
	 *
	 * @param areaId the id to get the reservations from
	 * @param start the start of the time search
	 * @param end the end of the time search
	 * @return a list of reservations for the area that occur within the given timeframe
	 */
	public List<Reservation> getReservationsForAreaBetween(
		UUID areaId, LocalDateTime start, LocalDateTime end
	) {
		return this.reservationRepository.findReservationsForAreaInTimePeriod(areaId, start, end);
	}

	/**
	 * Checks if the input area has any gap that is greater than the input duration.
	 *
	 * @param areaId the id of the area to check
	 * @param searchStart the time to start search from
	 * @param searchEnd the time to end the search
	 * @param minDuration the minimum length of a gap to find
	 * @return true if the area has a gap that is at least the size of {@code minDuration}
	 */
	public boolean doesAreaHaveFreeGapLike(
		UUID areaId,
		LocalDateTime searchStart,
		LocalDateTime searchEnd,
		Duration minDuration
	) {
		Iterator<Reservation> reservations = reservationRepository
			.findReservationsForAreaInTimePeriod(areaId, searchStart, searchEnd).iterator();

		boolean hasGap = false;
		LocalDateTime prevEnd = searchStart;

		while (reservations.hasNext() && !hasGap) {
			Reservation reservation = reservations.next();

			if (isGapGreaterThanDuration(prevEnd, reservation.getStart(), minDuration)) {
				hasGap = true;
			}

			prevEnd = reservation.getEnd().isBefore(searchEnd) ? reservation.getEnd() : searchEnd;
		}

		return hasGap;
	}

	private boolean isGapGreaterThanDuration(
		LocalDateTime gapStart,
		LocalDateTime gapEnd,
		Duration duration
	) {
		return Duration.between(gapStart, gapEnd).compareTo(duration) >= 0;
	}

	/**
	 * Gets all reservations belonging to a given user.
	 *
	 * @param userId the user to get the reservations of
	 * @param page the page to get
	 * @param size the amount of entries per page
	 * @return a list of reservation DTOs belonging to the given user id.
	 */
	public Page<Reservation> getReservationsByUserPaged(UUID userId, Integer page, Integer size) {
		Pageable paging = PageRequest.of(page, size);
		if (page < 0 || page == null) {
			page = 0;
		}
		if (size < 0 || size == null) {
			size = DEFAULT_PAGE_SIZE;
		}
		return this.reservationRepository.findAllByUserPaged(userId, paging);
	}
}
