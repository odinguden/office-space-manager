package no.ntnu.idata2900.group3.chairspace.exceptions;

/**
 * To be used by service classes when an element can not be found in the database.
 * Extends Exception
 */
public class ElementNotFoundException extends Exception {

	/**
	 * Constructs a new instance of element not found exception.
	 * The message should explain what element could not be found.
	 *
	 * @param message reason for throwing
	 */
	public ElementNotFoundException(String message) {
		super(message);
	}
}
