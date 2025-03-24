package no.ntnu.idata2900.group3.chairspace.exceptions;

/**
 * A checked error for when a area has to few administrators.
 *
 * @see Area
 */
public class AdminCountException extends Exception {

	/**
	 * Constructs a new exception with a message.
	 *
	 * @param message The detailed message
	 */
	public AdminCountException(String message) {
		super(message);
	}
}
