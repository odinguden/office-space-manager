package no.ntnu.idata2900.group3.chairspace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import no.ntnu.idata2900.group3.chairspace.entity.EntityInterface;
import no.ntnu.idata2900.group3.chairspace.service.EntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;


/**
 * A base for controllers that need to implement basic CRUD operations.
 *
 * <p>
 * Allows controllers to perform all the basic CRUD operations, including create, retrieve, update,
 * and delete. Additionally handles all potential error situations associated with those operations,
 * such as conflicts and missing values.
 * </p>
 *
 * <p>
 * Additionally includes modifiable security behaviour. See {@link PermissionManager}.
 * </p>
 *
 * @author Sigve Bjørkedal
 * @author Odin Lyngsgård
 * @see EntityInterface
 */
public abstract class AbstractController<EntityT extends EntityInterface<IdTypeT>, IdTypeT>
	extends PermissionManager {
	private EntityService<EntityT, IdTypeT> entityService;

	/**
	 * Creates a new controller based on the input repository.
	 *
	 * @param repository the repository containing the entities handled by this controller
	 */
	protected AbstractController(EntityService<EntityT, IdTypeT> entityService) {
		this.entityService = entityService;
	}

	/**
	 * Retrieves an entity from the repository.
	 *
	 * @param id the id of the object to be retrieved
	 * @return a response entity containing the retrieved object with the code 200 OK
	 * @throws ResponseStatusException 404 not found if the ID does not exist in the repository
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization included with the
	 *     request has insufficient permissions to get single entities.
	 */
	@GetMapping("/{id}")
	@Operation(
		summary = "Gets an entity",
		description = "Attempts to find an entity with the given ID"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found an entity with the given name"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users do not have access to read these entities"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to read these entitites"
			),
		@ApiResponse(
			responseCode = "404",
			description = "No entity with the given ID was found"
			)
	})
	public ResponseEntity<EntityT> getEntity(@PathVariable IdTypeT id) {
		this.hasPermissionToGet();

		EntityT entity = entityService.get(id);

		if (entity == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(entity, HttpStatus.OK);
	}

	/**
	 * Retrieves all the entities found in the repository.
	 *
	 * @return a response entity containing a list of all entities in the reposiitory, with the code
	 *     200 OK
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization included with the
	 *     request has insufficient permissions to get all entities.
	 */
	@GetMapping()
	@Operation(
		summary = "Gets all entities",
		description = "Gets all entities in the repository"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found all entities"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users do not have access to read these entities"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to read these entities"
			)
	})
	public ResponseEntity<List<EntityT>> getEntities() {
		this.hasPermissionToGetAll();
		return new ResponseEntity<>(entityService.getAll(), HttpStatus.OK);
	}

	/**
	 * Attempts to create a new entity in the repository.
	 *
	 * @param object the object to be added to the repository
	 * @return a response entity with the status code 201 created
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization included with the
	 *     request has insufficient permissions to create entities.
	 * @throws ResponseStatusException 409 conflict if the ID already exists in the repository
	 */
	@PostMapping()
	@Operation(
		summary = "Creates a new entity",
		description = "Attempts to create a new entity in the repository"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Created the new entity without problems"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to create new entities"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to create a new entity"
			),
		@ApiResponse(
			responseCode = "409",
			description = "Failed to create a new entity as it already exists"
			)
	})
	public ResponseEntity<String> postEntity(@RequestBody EntityT object) {
		this.hasPermissionToPost();
		if (!entityService.create(object)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * Attempts to update an existing entity in the repository.
	 *
	 * @param object a new entity to replace the existing entity
	 * @return a response entity with the status code 204 no content
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization included with the
	 *      request has insufficient permissions to update entities.
	 * @throws ResponseStatusException 404 not found upon attempting to update an entity that does
	 *      not exist
	 */
	@PutMapping()
	@Operation(
		summary = "Updates an entity",
		description = "Attempts to update an entity"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Successfully updated the entity"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to update entities"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to update entities"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Failed to update the entity as it doesn't exist"
			)
	})
	public ResponseEntity<String> putEntity(@RequestBody EntityT object) {
		this.hasPermissionToPut();
		if (!entityService.update(object)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Attempts to delete an entity from the repository.
	 *
	 * @param id the id of the entity to be deleted
	 * @return a response entity with the status code 204 no content
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization included with the
	 *      request has insufficient permissions to delete entities.
	 * @throws ResponseStatusException code 404 not found if the entity does not exist
	 */
	@DeleteMapping("/{id}")
	@Operation(
		summary = "Deletes an entity",
		description = "Attempts to delete an entity with the given ID"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Successfully deleted the entity"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to delete entities"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to delete entities"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Failed to delete the entity as it doesn't exist"
			)
	})
	public ResponseEntity<String> deleteEntity(@PathVariable IdTypeT id) {
		this.hasPermissionToDelete();
		if (!entityService.delete(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
