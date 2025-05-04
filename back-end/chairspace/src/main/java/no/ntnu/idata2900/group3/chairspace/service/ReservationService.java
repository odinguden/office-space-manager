package no.ntnu.idata2900.group3.chairspace.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.repository.ReservationRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with and managing {@link Reservation}s.
 *
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@Service
public class ReservationService extends EntityService<Reservation, UUID> {
	/** * The amount of milliseconds in a single full day. */
	public static final int MS_IN_DAY = 24 * 60 * 60 * 1000;
	private final ReservationRepository reservationRepository;

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

		boolean hasGap = true;
		LocalDateTime prevEnd = searchStart;

		while (reservations.hasNext() && hasGap) {
			Reservation reservation = reservations.next();

			if (!isGapGreaterThanDuration(prevEnd, reservation.getStart(), minDuration)) {
				hasGap = false;
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
	 * Gets the percentage of a day that an area is occupied by reservations.
	 *
	 * @param areaId the id of the area
	 * @param day the day to check the reservation frequency of
	 * @return the percentage of the day an area is occupied, as a decimal
	 * @see #getReservationFrequencyForMonth(UUID, YearMonth)
	 */
	public float getReservationFrequencyForDay(UUID areaId, LocalDate day) {
		LocalDateTime startOfDay = day.atStartOfDay();
		LocalDateTime endOfDay = day.plusDays(1).atStartOfDay();

		List<Reservation> reservationsForDay = reservationRepository
			.findReservationsForAreaInTimePeriod(areaId, startOfDay, endOfDay);

		float totalMilliseconds = 0;

		for (Reservation reservation : reservationsForDay) {
			LocalDateTime clampedStartTime = clampTime(reservation.getStart(), startOfDay, null);
			LocalDateTime clampedEndTime = clampTime(reservation.getEnd(), null, endOfDay);

			totalMilliseconds += getMillisBetween(clampedStartTime, clampedEndTime);
		}

		return totalMilliseconds / MS_IN_DAY;
	}

	/**
	 * Gets the frequency of reservations for every day in a month.
	 *
	 * @param areaId the id of the area
	 * @param year the year to which the month belongs
	 * @param month the month to check the reservation frequency of
	 * @return list of days with the percentage of the day an area is occupied, as a decimal
	 * @see #getReservationFrequencyForDay(UUID, LocalDate)
	 */
	public List<Float> getReservationFrequencyForDaysInMonth(UUID areaId, int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);

		List<Float> frequencies = new ArrayList<>();

		for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
			LocalDate thisDate = LocalDate.of(year, month, day);

			frequencies.add(getReservationFrequencyForDay(areaId, thisDate));
		}

		return frequencies;
	}

	/**
	 * Gets the percentage of a month that an area is occupied by reservations.
	 *
	 * @param areaId the id of the area
	 * @param year the year to which the month belongs
	 * @param month the month to check the reservation frequency of
	 * @return the percentage of the day an area is occupied, as a decimal
	 * @see #getReservationFrequencyForDay(UUID, LocalDate)
	 */
	public float getReservationFrequencyForMonth(UUID areaId, int year, int month) {
		// Note: Month is 1-indexed (range of 1 to 12)
		YearMonth yearMonth = YearMonth.of(year, month);
		int lengthOfMonth = yearMonth.lengthOfMonth();

		// We use a higher intermediary precision to increase accuracy
		// as floats lose accuracy as numbers grow large.
		double totalFrequency = 0;

		for (int day = 1; day <= lengthOfMonth; day++) {
			LocalDate thisDate = LocalDate.of(year, month, day);

			totalFrequency += getReservationFrequencyForDay(areaId, thisDate);
		}

		// Since max frequency where every day is fully occupied is the same as
		// the length of the month, this gets the percentage for the full month
		return (float) (totalFrequency / lengthOfMonth);
	}

	/**
	 * Clamps the time to a minimum and maximum time.
	 *
	 * @param time the time to be clamped
	 * @param min the lowest permissible value for the time. If missing, does not enforce a boundary
	 * @param max the highest permissible value for the time. If missing, does not enforce a
	 *     boundary
	 * @return the time clamped between min and max.
	 */
	private LocalDateTime clampTime(LocalDateTime time, LocalDateTime min, LocalDateTime max) {
		LocalDateTime clampedTime = time;

		if (min != null && time.isBefore(min)) {
			clampedTime = min;
		} else if (max != null && time.isAfter(max)) {
			clampedTime = max;
		}

		return clampedTime;
	}

	private float getMillisBetween(LocalDateTime start, LocalDateTime end) {
		Duration duration = Duration.between(start, end);

		return duration.toMillis();
	}
}
