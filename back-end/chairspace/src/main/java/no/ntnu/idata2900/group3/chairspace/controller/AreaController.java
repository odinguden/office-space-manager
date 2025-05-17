package no.ntnu.idata2900.group3.chairspace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.assembler.AreaAssembler;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleArea;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.exceptions.ElementNotFoundException;
import no.ntnu.idata2900.group3.chairspace.service.AreaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for the area entity.
 *
 * @see Area
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@RestController
@RequestMapping("/area")
public class AreaController extends PermissionManager {
	private final AreaService areaService;
	private final AreaAssembler areaAssembler;

	/**
	 * Creates a new area controller.
	 *
	 * @param areaService autowired area service
	 * @param areaAssembler autowired area assembler
	 */
	public AreaController(AreaService areaService, AreaAssembler areaAssembler) {
		this.areaService = areaService;
		this.areaAssembler = areaAssembler;
	}

	/**
	 * Retrieves a single area.
	 *
	 * @param id the id of the area to retrieve
	 * @return 200 OK with the retrieved area as a simple area
	 */
	@GetMapping("/{id}")
	@Operation(
		summary = "Gets a single area",
		description = "Returns a single simple area object for a area with the given id"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found a area with the given id"
			),
		@ApiResponse(
			responseCode = "404",
			description = "No area with the given id was found"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users do not have access to read areas"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to read areas"
			),
	})
	public ResponseEntity<SimpleArea> get(
		@Parameter(description = "Id of area to get") @PathVariable UUID id
	) {
		this.hasPermissionToGet();
		Area area = this.areaService.get(id);

		if (area == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(areaAssembler.toSimpleArea(area), HttpStatus.OK);
	}

	/**
	 * Retrieves all areas.
	 *
	 * @param page the page of the pagination to retrieve an area from, defaults to 0
	 * @return 200 OK with a pagination containing all areas as simple areas
	 */
	@GetMapping("")
	@Operation(
		summary = "Returns all areas",
		description = "Returns all areas paginated."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "returns the requested page of all areas"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users do not have access to read areas"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to read areas"
			),

	})
	public ResponseEntity<Page<SimpleArea>> getAll(
		@Parameter(description = "The page that is requested. Will be 0 if not included")
		@RequestParam(required = false) Integer page
	) {
		if (page == null || page < 0) {
			page = 0;
		}
		this.hasPermissionToGetAll();
		Page<Area> areas = areaService.getAllPaged(page);
		Page<SimpleArea> simpleAreas = areas.map(areaAssembler::toSimpleArea);
		return new ResponseEntity<>(simpleAreas, HttpStatus.OK);
	}

	/**
	 * Returns a list of areas that contain reservations, specifically for the homepage.
	 *
	 * @param page the page of the pagination to retrieve areas from.
	 * @return 200 OK with a pagination of areas with reservations for the next 12 hours
	 */
	@GetMapping("/home")
	@Operation(
		summary = "Returns areas for homepage",
		description = "Returns areas that contain reservations, specifically for the homepage"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = ""
			)
	})
	public ResponseEntity<Page<SimpleArea>> getHome(
		@Parameter(description = "The page that is requested. will be 0 if not included")
		@RequestParam(required = false) Integer page
	) {
		if (page == null || page < 0) {
			page = 0;
		}

		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now().plusHours(12);

		this.hasPermissionToGetAll();
		Page<Area> areas = areaService.getAllPaged(page);
		Page<SimpleArea> simpleAreas = areas.map(
			area -> areaAssembler.toSimpleAreaWithReservations(area, start, end)
		);
		return new ResponseEntity<>(simpleAreas, HttpStatus.OK);
	}

	/**
	 * Creates a new area, granted one doesn't already exist.
	 *
	 * @param simpleArea a simplified area container to create from
	 * @return 201 CREATED
	 */
	@PostMapping("")
	@Operation(
		summary = "Creates a new area",
		description = "Creates a new area, granted that one does not already exist"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Created entity"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to create new areas"
			),
		@ApiResponse(
			responseCode = "400",
			description = "Not able to create a new area with the provided data"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Id provided in request does not correspond to an entity in the database"
			)
	})
	public ResponseEntity<UUID> post(
		@Parameter(description = "Data used to create a area")
		@RequestBody SimpleArea simpleArea
	) {
		this.hasPermissionToPost();
		Area area;

		try {
			area = areaAssembler.assembleArea(simpleArea);
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		UUID id = areaService.create(area);
		return new ResponseEntity<>(id, HttpStatus.CREATED);
	}

	/**
	 * Updates an existing area.
	 *
	 * @param simpleArea the simple area containing update data. Values not to be updated should be
	 *     null
	 * @return 204 NO CONTENT
	 */
	@PutMapping("")
	@Operation(
		summary = "Updates an existing area",
		description = "Updates a area based data from the simple area object."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Object was updated"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Object to update was not found"
			),
		@ApiResponse(
			responseCode = "400",
			description = "Not able to map simple area object provided to an area"
			)
	})
	public ResponseEntity<String> put(
		@Parameter(description = "Object containing data used to update area."
			+ " Values not to be updated should be null.")
		@RequestBody SimpleArea simpleArea
	) {
		this.hasPermissionToPut();

		Area area;
		try {
			area = areaAssembler.mergeWithExisting(simpleArea.id(), simpleArea);
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		areaService.update(area);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Deletes the area with the provided id.
	 *
	 * @param id the id of the area to be deleted
	 * @return 204 NO CONTENT
	 */
	@DeleteMapping("/{id}")
	@Operation(
		summary = "Deletes an area",
		description = "Deletes a area based on the provided id"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Element deleted"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to delete entities"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to delete entities"
			),
	})
	public ResponseEntity<String> delete(
		@Parameter(description = "Id of the area to delete")
		@PathVariable UUID id
	) {
		this.hasPermissionToDelete();
		areaService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Gets all areas that have this user as an admin.
	 *
	 * @param userId the id of user to find areas for
	 * @param page page index
	 * @param size number of entries per page
	 * @return a page of areas that have this user as an admin
	 */
	@GetMapping("/user/{userId}")
	@Operation(
		summary = "Gets all areas administered by a single user",
		description = " "
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Areas found with"
			),
		@ApiResponse(
			responseCode = "",
			description = ""
			)
	})
	public ResponseEntity<Page<SimpleArea>> findAreasByAdmin(
		@Parameter(description = "Id of user to get areas for")
		@PathVariable UUID userId,
		@Parameter(description = "pagination page")
		@RequestParam(required = false) Integer page,
		@Parameter(description = "Number of items included on the page")
		@RequestParam(required = false) Integer size
	) {
		this.hasPermissionToGetAll();
		Page<Area> areas = areaService.getAreasByUser(userId, page, size);
		return new ResponseEntity<>(
			areas.map(areaAssembler::toSimpleArea),
			HttpStatus.OK
		);
	}

	@GetMapping("/superareas")
	public ResponseEntity<List<SimpleArea>> findSuperAreasByName(@RequestParam String name) {
		this.hasPermissionToGet();

		return new ResponseEntity<>(
			areaService.getSuperAreasByName(name)
				.stream()
				.map(areaAssembler::toSimpleSuperArea)
				.toList(),
			HttpStatus.OK
		);
	}
}
