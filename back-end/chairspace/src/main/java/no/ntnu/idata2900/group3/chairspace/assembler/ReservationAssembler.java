package no.ntnu.idata2900.group3.chairspace.assembler;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleReservation;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.NotReservableException;
import no.ntnu.idata2900.group3.chairspace.service.AreaService;
import no.ntnu.idata2900.group3.chairspace.service.UserService;
import org.springframework.stereotype.Component;

/**
 * A utility class that helps convert the Reservation class to and from its respective DTOs.
 */
@Component
public class ReservationAssembler {
	private final AreaService areaService;
	private final UserService userService;

	/**
	 * Creates a new Reservation Assembler.
	 *
	 * @param areaService the area service connected to this assembler
	 * @param userService the user service connected to this assembler
	 */
	public ReservationAssembler(AreaService areaService, UserService userService) {
		this.areaService = areaService;
		this.userService = userService;
	}

	/**
	 * Projects a reservation to a simple reservation.
	 *
	 * @param reservation the reservation to project
	 * @return the simple representation of the input reservation
	 */
	public SimpleReservation toSimple(Reservation reservation) {
		User sessionUser = userService.getSessionUser();
		SimpleReservation.Builder builder = SimpleReservation.Builder.fromReservation(reservation);

		System.out.println("TESTING USER STUFF -------==================");
		if (sessionUser != null) {
			System.out.println(reservation.getUser().getId());
			System.out.println(sessionUser.getId());
			builder.isMine(reservation.getUser().getId().equals(sessionUser.getId()));
		}

		return builder.build();
	}

	/**
	 * Converts a simple reservation to its domain entity.
	 *
	 * @param simpleReservation the simple reservation to convert
	 * @return a reservation based on the input simple reservation
	 * @throws InvalidArgumentCheckedException if the input contains certain null parameters
	 * @throws NotReservableException if the input area is not a reservable area
	 */
	public Reservation toDomain(SimpleReservation simpleReservation)
		throws InvalidArgumentCheckedException, NotReservableException {
		UUID areaId = simpleReservation.areaId();
		UUID userId = simpleReservation.userId();

		Area area = areaService.get(areaId);
		User user = userService.get(userId);

		Reservation reservation;

		if (simpleReservation.comment() == null) {
			reservation = new Reservation(
				area,
				user,
				simpleReservation.startTime(),
				simpleReservation.endTime()
			);
		} else {
			reservation = new Reservation(
				area,
				user,
				simpleReservation.startTime(),
				simpleReservation.endTime(),
				simpleReservation.comment()
			);
		}

		return reservation;
	}
}
