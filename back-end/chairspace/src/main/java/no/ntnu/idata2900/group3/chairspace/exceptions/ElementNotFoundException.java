package no.ntnu.idata2900.group3.chairspace.exceptions;

import java.util.UUID;

/**
 * To be used by service classes when an element can not be found in the database.
 * Extends Exception
 */
public final class ElementNotFoundException extends Exception {

	/**
	 * Constructs a new instance of element not found exception.
	 * The message should explain what element could not be found.
	 *
	 * @param message reason for throwing
	 */
	private ElementNotFoundException(String message) {
		super(message);
	}

	/**
	 * Creates new instance of ElementNotFoundException with predefined text for if a
	 * generic entity could not be found.
	 *
	 * @return new ElementCouldNotBeFound instance with matching message
	 */
	public static ElementNotFoundException entityNotFoundException() {
		return new ElementNotFoundException(
			"Could not find item"
		);
	}

	/**
	 * Creates new instance of ElementNotFoundException with predefined text
	 * for if an area was not found.
	 *
	 * @return new ElementNotFoundException with matching error message
	 */
	public static ElementNotFoundException areaNotFoundException() {
		return new ElementNotFoundException(
			"Could not find area"
		);
	}

	/**
	 * Creates new instance of ElementNotFoundException with predefined text
	 * for if a area feature was not found.
	 *
	 * @return new ElementNotFoundException with matching error message
	 */
	public static ElementNotFoundException areaFeatureNotFoundException() {
		return new ElementNotFoundException(
			"Could not find area feature"
		);
	}

	/**
	 * Creates new instance of ElementNotFoundException with predefined text
	 * for if a area type is not found in the database.
	 *
	 * @return new ElementNotFoundException with matching error message
	 */
	public static ElementNotFoundException areaTypeNotFoundException() {
		return new ElementNotFoundException(
			"Could not find area type"
		);
	}

	/**
	 * Creates new instance of ElementNotFoundException with predefined text
	 * for if a user is not found in the database.
	 *
	 * @return new ElementNotFoundException with matching error message
	 */
	public static ElementNotFoundException userNotFoundException() {
		return new ElementNotFoundException(
			"Could not find user"
		);
	}
}
