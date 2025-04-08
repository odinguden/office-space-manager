package no.ntnu.idata2900.group3.chairspace.exceptions;

import java.util.UUID;

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

	/**
	 * Creates new instance of ElementNotFoundException with predefined text
	 * for if an area was not found.
	 *
	 * @param areaId id of the area that could not be found
	 * @return new ElementNotFoundException with matching error message
	 */
	public static ElementNotFoundException areaNotFoundException(UUID areaId) {
		return new ElementNotFoundException(
			"Could not find area with id: " + areaId
		);
	}

	/**
	 * Creates new instance of ElementNotFoundException with predefined text
	 * for if a area feature was not found.
	 *
	 * @param featureId id of the area feature that could not be found
	 * @return new ElementNotFoundException with matching error message
	 */
	public static ElementNotFoundException areaFeatureNotFoundException(String featureId) {
		return new ElementNotFoundException(
			"Could not find area feature with name: " + featureId
		);
	}
}
