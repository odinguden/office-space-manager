package no.ntnu.idata2900.group3.chairspace.exceptions;

import no.ntnu.idata2900.group3.chairspace.entity.Reservation;

/**
 * Exception for when a reservation is reserved.
 */
public class ReservedException extends Exception {

	/**
	 * Constructor for ReservedException.
	 *
	 * @param message the message to display
	 */
	public ReservedException(String message) {
		super(message);
	}

	/**
	 * Constructor for ReservedException. With a message and a blocking reservation.
	 *
	 * @param blockingReservation the reservation that is blocking the reservation
	 */
	public ReservedException(Reservation blockingReservation) {
		super(
			"Action blocked by reservation starting at "
			+ blockingReservation.getStart().toString()
			+ " and ending at " + blockingReservation.getEnd().toString()
		);
	}
}
