package no.ntnu.idata2900.group3.chairspace.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.reservation.ReservationDto;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing and interacting with reservations.
 */
@Service
public class ReservationService {
	public static final int MS_IN_DAY = 24 * 60 * 60 * 1000;

	@Autowired
	ReservationRepository reservationRepository;

	/**
	 * Gets all the reservations that exist for an area within the specified time period.
	 *
	 * @param areaId the id of the area to get the time from
	 * @param from the start time to get reservations from
	 * @param until the end time to get reservations until
	 * @return a list containing all reservations that exist within the specified time period
	 *     for the given room
	 */
	public List<ReservationDto> getReservationsForAreaInTimePeriod(
		UUID areaId,
		LocalDateTime from,
		LocalDateTime until
	) {
		List<Reservation> rawList = reservationRepository
			.findReservationsForAreaInTimePeriod(areaId, from, until);

		return rawList.stream()
			.map(ReservationDto::new)
			.toList();
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
	 * Gets the percentage of a month that an area is occupied by reservations.
	 *
	 * @param areaId the id of the area
	 * @param yearMonth the month to check the reservation frequency of
	 * @return the percentage of the day an area is occupied, as a decimal
	 * @see #getReservationFrequencyForDay(UUID, LocalDate)
	 */
	public float getReservationFrequencyForMonth(UUID areaId, YearMonth yearMonth) {
		int lengthOfMonth = yearMonth.lengthOfMonth();
		int year = yearMonth.getYear();
		Month month = yearMonth.getMonth();

		// We use a higher intermediary precision to increase accuracy
		// as floats lose accuracy as numbers grow large.
		double millisecondsInMonth = (MS_IN_DAY * lengthOfMonth);
		double totalMilliseconds = 0;

		for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
			LocalDate thisDate = LocalDate.of(year, month, day);

			totalMilliseconds += getReservationFrequencyForDay(areaId, thisDate);
		}

		return (float) (totalMilliseconds / millisecondsInMonth);
	}

	private LocalDateTime clampTime(LocalDateTime time, LocalDateTime min, LocalDateTime max) {
		// Multiple returns. Your mom.
		if (min != null && time.isBefore(min)) {
			return min;
		} else if (max != null && time.isAfter(max)) {
			return max;
		}
		return time;
	}

	private float getMillisBetween(LocalDateTime start, LocalDateTime end) {
		Duration duration = Duration.between(start, end);

		return duration.toMillis();
	}
}
