package no.ntnu.idata2900.group3.chairspace.controller;

import org.springframework.web.server.ResponseStatusException;

/**
 * A base for controllers that need to implement permission control.
 *
 * <p>
 * Includes overridable methods for common security cases.
 * These methods will throw a ResponseStatusException.
 * This exception is caught by spring and translated into a http response.
 *
 * @author Sigve Bjørkedal
 * @author Odin Lyngsgård
 */
public abstract class PermissionManager {

	/**
	 * Checks if the current session user has permission to get a single entity.
	 *
	 * <p>
	 * This method is meant to be overridden if abnormal permissions management is required.
	 * </p>
	 *
	 * @return true if the user has sufficient permissions
	 * @throws ResponseStatusException 401 unauthorized if the current user lacks authorization
	 * @throws ResponseStatusException 403 forbidden if the current user lacks permissions
	 */
	protected boolean hasPermissionToGet() {
		return true;
	}

	/**
	 * Checks if the current session user has permission to get all entities in the repository.
	 *
	 * <p>
	 * This method is meant to be overridden if abnormal permissions management is required.
	 * </p>
	 *
	 * @return true if the user has sufficient permissions
	 * @throws ResponseStatusException 401 unauthorized if the current user lacks authorization
	 * @throws ResponseStatusException 403 forbidden if the current user lacks permissions
	 */
	protected boolean hasPermissionToGetAll() {
		return true;
	}

	/**
	 * Checks if the current session user has permission to create new entities.
	 *
	 * <p>
	 * This method is meant to be overridden if abnormal permissions management is required.
	 * </p>
	 *
	 * @return true if the user has sufficient permissions
	 * @throws ResponseStatusException 401 unauthorized if the current user lacks authorization
	 * @throws ResponseStatusException 403 forbidden if the current user lacks permissions
	 */
	protected boolean hasPermissionToPost() {
		return true;
	}

	/**
	 * Checks if the current session user has permission to update entities.
	 *
	 * <p>
	 * This method is meant to be overridden if abnormal permissions management is required.
	 * </p>
	 *
	 * @return true if the user has sufficient permissions
	 * @throws ResponseStatusException 401 unauthorized if the current user lacks authorization
	 * @throws ResponseStatusException 403 forbidden if the current user lacks permissions
	 */
	protected boolean hasPermissionToPut() {
		return true;
	}

	/**
	 * Checks if the current session user has permission to delete entities.
	 *
	 * <p>
	 * This method is meant to be overridden if abnormal permissions management is required.
	 * </p>
	 *
	 * @return true if the user has sufficient permissions
	 * @throws ResponseStatusException 401 unauthorized if the current user lacks authorization
	 * @throws ResponseStatusException 403 forbidden if the current user lacks permissions
	 */
	protected boolean hasPermissionToDelete() {
		return true;
	}
}
