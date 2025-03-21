package no.ntnu.idata2900.group3.chairspace.exceptions;

/**
 * Exception that is thrown when an attempt is made to reserve an area that is not reservable.
 */
public final class NotReservableException extends Exception {

	/**
	 * Constructs a new NotReservableException with a message.
	 *
	 * @param message message to display
	 */
	private NotReservableException(String message) {
		super(message);
	}

	/**
	 * Constructs a new NotReservableException with message
	 *     "Cannot create reservation for non reservable area".
	 *
	 * @return not reservable exception with message
	 */
	public static NotReservableException overlapException() {
		return new NotReservableException("Cannot create reservation for non reservable area");
	}
}
