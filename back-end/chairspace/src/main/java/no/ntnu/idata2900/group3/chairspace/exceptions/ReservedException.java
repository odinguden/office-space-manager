package no.ntnu.idata2900.group3.chairspace.exceptions;

import no.ntnu.idata2900.group3.chairspace.entity.Reservation;

/**
 * Exception for when a reservation is reserved.
 */
public final class ReservedException extends Exception {

	/**
	 * Constructor for ReservedException.
	 *
	 * @param message the message to display
	 */
	private ReservedException(String message) {
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

	/**
	 * Creates instance of ReservedException with message:
	 *     "Cannot create reservation, reservation clashes with existing reservation".
	 *
	 * @return instance of ReservedException with message for overlapping reservation
	 */
	public static ReservedException reservationOverlapException() {
		return new ReservedException(
			"Cannot create reservation, reservation clashes with existing reservation"
		);
	}
}
