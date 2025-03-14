package no.ntnu.idata2900.group3.chairspace.exceptions;

/**
 * Exception thrown when an invalid argument is passed to a method.
 * Checked exception.
 */
public class InvalidArgumentCheckedException extends Exception {

	/**
	 * Constructs a new InvalidArgumentCheckedException with the specified detail message.
	 *
	 * @param message the detail message.
	 */
	public InvalidArgumentCheckedException(String message) {
		super(message);
	}

	/**
	 * Constructs a new InvalidArgumentCheckedException.
	 *
	 */
	public InvalidArgumentCheckedException() {
		super("");
	}
}
