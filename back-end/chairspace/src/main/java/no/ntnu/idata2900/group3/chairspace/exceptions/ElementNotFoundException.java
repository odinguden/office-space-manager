package no.ntnu.idata2900.group3.chairspace.exceptions;

/**
 * To be used by service classes when an element can not be found in the database.
 * Extends Exception
 */
public final class ElementNotFoundException extends Exception {
	public static final ElementNotFoundException ENTITY_NOT_FOUND =
		new ElementNotFoundException("Could not find item");
	public static final ElementNotFoundException AREA_NOT_FOUND =
		new ElementNotFoundException("Could not find area");
	public static final ElementNotFoundException AREA_FEATURE_NOT_FOUND =
		new ElementNotFoundException("Could not find area feature");
	public static final ElementNotFoundException AREA_TYPE_NOT_FOUND =
		new ElementNotFoundException("Could not find area type");
	public static final ElementNotFoundException USER_NOT_FOUND =
		new ElementNotFoundException("Could not find user");
	public static final ElementNotFoundException RESERVATION_NOT_FOUND =
		new ElementNotFoundException("Could not find reservation");

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
