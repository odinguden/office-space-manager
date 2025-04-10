package no.ntnu.idata2900.group3.chairspace.exceptions;

/**
 * To be used by service classes when an element can not be found in the database.
 * Extends Exception
 */
public final class ElementNotFoundException extends Exception {
	public static final ElementNotFoundException entityNotFoundException =
		new ElementNotFoundException("Could not find item");
	public static final ElementNotFoundException areaNotFoundException =
		new ElementNotFoundException("Could not find area");
	public static final ElementNotFoundException areaFeatureNotFoundException =
		new ElementNotFoundException("Could not find area feature");
	public static final ElementNotFoundException areaTypeNotFoundException =
		new ElementNotFoundException("Could not find area type");
	public static final ElementNotFoundException userNotFoundException =
		new ElementNotFoundException("Could not find user");

	/**
	 * Constructs a new instance of element not found exception.
	 * The message should explain what element could not be found.
	 *
	 * @param message reason for throwing
	 */
	private ElementNotFoundException(String message) {
		super(message);
	}
}
