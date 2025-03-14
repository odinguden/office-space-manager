package no.ntnu.idata2900.group3.chairspace.exceptions;

/**
 * Exception for when a user is not authorized to perform an action.
 */
public class AuthorizationException extends Exception {

	/**
	 * Constructor for AuthorizationException.
	 *
	 * @param message the message to display
	 */
	public AuthorizationException(String message) {
		super(message);
	}
}
