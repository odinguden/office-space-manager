package no.ntnu.idata2900.group3.chairspace.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.NotReservableException;
import no.ntnu.idata2900.group3.chairspace.exceptions.ReservedException;
import no.ntnu.idata2900.group3.chairspace.repository.AreaRepository;
import no.ntnu.idata2900.group3.chairspace.repository.ReservationRepository;
import no.ntnu.idata2900.group3.chairspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service class for the Reservation entity.
 * This class is responsible for creating reservations and checking for conflicts.
 */
@Service
public class ReservationService {
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private UserRepository userRepository;

	/**
	 * No args constructor for JPA.
	 */
	public ReservationService() {
		// No args constructor for JPA.
	}

	/**
	 * Creates a reservation for the given area and user.
	 *
	 * @param area the area to reserve
	 * @param user the user reserving the area
	 * @param start the start date and time of the reservation
	 * @param end the end date and time of the reservation
	 * @param comment a comment for the reservation
	 * @return the created reservation
	 * @throws InvalidArgumentCheckedException if the area or user is not found
	 * @throws IllegalArgumentException if the start date and time is after the end date and time
	 * @throws ReservedException if the area is already reserved for the given time period
	 * @throws NotReservableException if the area is not reservable
	 */
	public Reservation createReservation(
		Area area,
		User user,
		LocalDateTime start,
		LocalDateTime end,
		String comment
	) throws InvalidArgumentCheckedException, ReservedException, NotReservableException {
		area = getArea(area.getId());
		user = getUser(user.getId());

		if (checkAreaForConflict(area, start, end)) {
			throw ReservedException.reservationOverlapException();
		}

		return new Reservation(area, user, start, end, comment);
	}

	/**
	 * Creates a reservation for the given area and user.
	 *
	 * @param area the area to reserve
	 * @param start the start date and time of the reservation
	 * @param end the end date and time of the reservation
	 * @return true if there are conflicts, false otherwise
	 */
	private boolean checkAreaForConflict(Area area, LocalDateTime start, LocalDateTime end) {
		List<Reservation> reservations = reservationRepository.findByArea(area);

		return reservations.stream()
		.noneMatch(r -> r.doesCollide(start, end));
	}

	private Area getArea(UUID id) throws InvalidArgumentCheckedException {
		Area area = areaRepository.findById(id).orElse(null);
		if (area == null) {
			throw new InvalidArgumentCheckedException("Area not found");
		}
		return area;
	}

	private User getUser(UUID id) throws InvalidArgumentCheckedException {
		User user = userRepository.findById(id).orElse(null);
		if (user == null) {
			throw new InvalidArgumentCheckedException("User not found");
		}
		return user;
	}
}
