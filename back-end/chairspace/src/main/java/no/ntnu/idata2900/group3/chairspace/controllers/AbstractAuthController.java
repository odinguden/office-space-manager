package no.ntnu.idata2900.group3.chairspace.controllers;

import org.springframework.web.server.ResponseStatusException;

/**
 * @author Sigve Bjørkedal
 * @author Odin Lyngsgård
 */
public class AbstractAuthController {
	
	protected AbstractAuthController() {

	}

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
		// TODO: Implement security
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
		// TODO: Implement security
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
		// TODO: Implement security
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
		// TODO: Implement security
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
		// TODO: Implement security
		return true;
	}
}
