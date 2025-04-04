package no.ntnu.idata2900.group3.chairspace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaCreationDto;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaDto;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaModificationDto;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.AdminCountException;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.repository.AreaFeatureRepository;
import no.ntnu.idata2900.group3.chairspace.repository.AreaRepository;
import no.ntnu.idata2900.group3.chairspace.repository.AreaTypeRepository;
import no.ntnu.idata2900.group3.chairspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;




/**
 * Controller for the area entity.
 *
 * @see Area
 * @author Odin Lyngsgård
 */
@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/area")
public class AreaController extends AbstractPermissionManager {

	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private AreaTypeRepository areaTypeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AreaFeatureRepository areaFeatureRepository;

	/**
	 * Posts a new area to the database based on the data from AreaDTO.
	 *
	 * @param areaDto DTO containing data relating to a area
	 * @return a response entity with the status code 201 created
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization included with the
	 *     request has insufficient permissions to create entities.
	 * @throws ResponseStatusException 409 conflict if the ID already exists in the repository
	 * @throws ResponseStatusException 400 bad request if data in the DTO is not valid for creation
	 *     of an area.
	 * @throws ResponseStatusException code 404 if the super area included in the dto does not exist
	 * @throws ResponseStatusException code 404 if the area feature included in the dto does not exist
	 * @throws ResponseStatusException code 404 if the area included in the dto type does not exist
	 */
	@PostMapping()
	@Operation(
		summary = "Creates a new area",
		description = "Attempts to create a new entity using data from the AreaCreationDto"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Successfully created a new area"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to create areas"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to create areas"
			),
		@ApiResponse(
			responseCode = "400",
			description = "Failed to create area with the information contained within the area DTO"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Could not find super area from Id in area creation dto"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Could not find areaType from Id in area creation dto"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Could not find areaFeature from Id in area creation dto"
			)
	})
	public ResponseEntity<String> postEntity(@RequestBody AreaCreationDto areaDto) {
		super.hasPermissionToPost();
		Area area = buildArea(areaDto);
		areaRepository.save(area);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * Returns area with the given id.
	 *
	 * @param id uuid of area
	 * @return area
	 * @throws ResponseStatus exception code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatus exception code 403 forbidden if the authorization included is not sufficient to get area
	 * @throws ResponseStatus exception code 404 if no area can be found with this id
	 */
	@GetMapping("/{id}")
	@Operation(
		summary = "Gets an area",
		description = "Attempts to find an area with the given ID"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found an area with the given name"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users do not have access to read these areas"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to read these areas"
			),
		@ApiResponse(
			responseCode = "404",
			description = "No area with the given ID was found"
			)
	})
	public ResponseEntity<AreaDto> getArea(@PathVariable UUID id) {
		hasPermissionToGet();
		Optional<Area> optionalArea = areaRepository.findById(id);
		if (!optionalArea.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		AreaDto areaDto = new AreaDto(optionalArea.get());
		return new ResponseEntity<>(areaDto, HttpStatus.OK);
	}

	/**
	 * Returns all areas from the database as area DTO's.
	 *
	 * @return all areas in the database in list
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization
	 *     included is not sufficient to get all areas
	 */
	@GetMapping("")
	@Operation(
		summary = "Gets all Areas",
		description = "Gets all areas in the repository"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found all areas"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users do not have access to read these areas"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to read these areas"
			)
	})
	public ResponseEntity<List<AreaDto>> getArea() {
		hasPermissionToGetAll();
		Iterator<Area> it = areaRepository.findAll().iterator();
		List<AreaDto> areas = new ArrayList<>();

		while (it.hasNext()) {
			areas.add(
				new AreaDto(it.next())
			);
		}
		return new ResponseEntity<>(areas, HttpStatus.OK);
	}

	/**
	 * Updates an area in the database.
	 *
	 * @param areaDto area DTO representing the area to update.
	 * @return response entity with status 204 no content
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization included with the
	 *      request has insufficient permissions to update entities.
	 * @throws ResponseStatusException 404 not found upon attempting to update an area that does
	 *      not exist
	 */
	@PutMapping()
	@Operation(
		summary = "Updates an area",
		description = "Attempts to update an area"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Successfully updated the area"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to update area"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to update area"
			),
		@ApiResponse(
			responseCode = "400",
			description = "Bad request if not able to update area with the information contained in the dto"
			)
	})
	public ResponseEntity<String> putArea(@RequestBody AreaModificationDto areaDto) {
		super.hasPermissionToPut();
		// Throws 404 if area cannot be found
		Area area = getAreaFromId(areaDto.getId());
		try {
			area.updateName(areaDto.getName());
		} catch (InvalidArgumentCheckedException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		area.updateDescription(areaDto.getDescription());
		try {
			area.setReservable(areaDto.isReservable());
		} catch (InvalidArgumentCheckedException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		try {
			area.updateCapacity(areaDto.getCapacity());
		} catch (InvalidArgumentCheckedException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		// Throws 404 if area type cannot be found
		AreaType areaType = getAreaType(areaDto.getAreaType());
		area.updateAreaType(areaType);
		area.updateCalendarLink(areaDto.getCalendarLink());
		areaRepository.save(area);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}


	/**
	 * Attempts to delete a area from the repository.
	 *
	 * @param id the id of the area to be deleted
	 * @return a response entity with the status code 204 no content
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization included with the
	 *      request has insufficient permissions to delete entities.
	 * @throws ResponseStatusException code 404 not found if the entity does not exist
	 */
	@DeleteMapping("/{id}")
	@Operation(
		summary = "Deletes an area",
		description = "Attempts to delete a area with the given ID"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Successfully deleted the area"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to areas entities"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to delete area"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Failed to delete the area as it doesn't exist"
			)
	})
	public ResponseEntity<String> deleteArea(@PathVariable UUID id) {
		this.hasPermissionToDelete();

		Optional<Area> entity = areaRepository.findById(id);

		if (!entity.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		areaRepository.delete(entity.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

	// ----- Methods for Area Creation -----

	/**
	 * Builds area from regular data DTO.
	 *
	 * @param areaDto the Dto used to build the area
	 * @return new Area Object
	 * @throws ResponseStatusException code 404 if the area does not exist
	 * @throws ResponseStatusException code 404 if the area feature does not exist
	 * @throws ResponseStatusException code 404 if the area type does not exist
	 */
	private Area buildArea(AreaDto areaDto) {
		AreaType areaType = getAreaType(areaDto.getAreaType().getId());

		// Create builder with initial arguments
		Area.Builder areaBuilder;
		try {
			areaBuilder = new Area.Builder(
				areaDto.getName(),
				areaDto.getCapacity(),
				areaType
			);
		} catch (InvalidArgumentCheckedException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		areaBuilder.id(areaDto.getId());

		// Adds administrators to area builder
		for (UUID id : areaDto.getAdministrators()) {
			areaBuilder.administrator(
				getUser(id)
			);
		}

		try {
			areaBuilder.calendarLink(areaDto.getCalendarLink());
		} catch (InvalidArgumentCheckedException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		// These do not need any checks so they're just added
		areaBuilder.description(areaDto.getDescription());
		areaBuilder.reservable(areaDto.isReservable());

		// Add super area if exists
		if (areaDto.getIdOfSuperArea() != null) {
			areaBuilder.superArea(
				getAreaFromId(areaDto.getIdOfSuperArea())
			);
		}

		// Add area features
		for (AreaFeature areaFeature : areaDto.getAreaFeatures()) {
			String areaFeatureId = areaFeature.getId();
			areaBuilder.feature(
				getAreaFeature(areaFeatureId)
			);
		}

		// Finally build area
		Area area;
		try {
			area = areaBuilder.build();
		} catch (AdminCountException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return area;
	}

	/**
	 * Builds an area object from an area creation DTO.
	 *
	 * @param areaDto The area DTO to be used as base to build area object
	 * @return area built from dto
	 * @throws ResponseStatusException code 404 if the area does not exist
	 * @throws ResponseStatusException code 404 if the area feature does not exist
	 * @throws ResponseStatusException code 404 if the area type does not exist
	 */
	private Area buildArea(AreaCreationDto areaDto) {
		AreaType areaType = getAreaType(areaDto.getAreaType());

		// Create builder with initial arguments
		Area.Builder areaBuilder;
		try {
			areaBuilder = new Area.Builder(
				areaDto.getName(),
				areaDto.getCapacity(),
				areaType
			);
		} catch (InvalidArgumentCheckedException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		// Adds administrators to area builder
		for (UUID id : areaDto.getAdministrators()) {
			areaBuilder.administrator(
				getUser(id)
			);
		}

		try {
			areaBuilder.calendarLink(areaDto.getCalendarLink());
		} catch (InvalidArgumentCheckedException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		// These do not need any checks so they're just added
		areaBuilder.description(areaDto.getDescription());
		areaBuilder.reservable(areaDto.isReservable());

		// Add super area if exists
		if (areaDto.getSuperArea() != null) {
			areaBuilder.superArea(
				getAreaFromId(areaDto.getSuperArea())
			);
		}

		// Add area features
		for (String areaFeature : areaDto.getAreaFeatures()) {
			areaBuilder.feature(
				getAreaFeature(areaFeature)
			);
		}

		// Finally build area
		Area area;
		try {
			area = areaBuilder.build();
		} catch (AdminCountException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return area;
	}

	/**
	 * Gets an area from the database.
	 *
	 * @param id the id of the area to get
	 * @return an area from the database
	 * @throws ResponseStatusException code 404 if the area does not exist
	 */
	public Area getAreaFromId(UUID id) {
		Optional<Area> optional = areaRepository.findById(id);
		if (!optional.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return optional.get();
	}

	/**
	 * Returns an area feature based on id.
	 *
	 * @param id string id
	 * @return Area feature from the database
	 * @throws ResponseStatusException code 404 if the area feature does not exist in the database
	 */
	private AreaFeature getAreaFeature(String id) {
		Optional<AreaFeature> optionalAreaFeature = areaFeatureRepository.findById(id);
		if (!optionalAreaFeature.isPresent()) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"Cannot create area with feature: " + id + " as it does not exist in database"
			);
		}
		return optionalAreaFeature.get();
	}

	private User getUser(UUID id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent()) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"Could not find user with id: " + id.toString()
				+ ". Unable to create area with this user as administrator."
			);
		}
		return optionalUser.get();
	}

	/**
	 * Returns AreaType based on id.
	 *
	 * @param id id of area type
	 * @return AreaType from database
	 * @throws ResponseStatusException code 404 if the area type does not exist in the database
	 */
	private AreaType getAreaType(String id) {
		Optional<AreaType> optionalAreaType = areaTypeRepository.findById(id);
		if (!optionalAreaType.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
			"Cannot create area with provided area type, as it does not exist in database"
			);
		}
		return optionalAreaType.get();
	}

	/**
	 * Adds an area feature to an area.
	 *
	 * @param areaId id of the area to add the feature to
	 * @param areaFeatureId id of the area feature to add to the area
	 * @return response entity with status 204 no content
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization included with the
	 *      request has insufficient permissions to update entities.
	 * @throws ResponseStatusException code 404 not found upon attempting to update an area that
	 *      does not exist
	 * @throws ResponseStatusException code 404 not found upon attempting to add an areaFeature that
	 *      does not exist
	 */
	@PostMapping("/addAreaFeature/{areaId}/{areaFeatureId}")
	@Operation(
		summary = "Adds an area feature to an area",
		description = "Attempts to add an area feature to an area"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Successfully added area feature to area"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to add area features to areas"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to add area features to areas"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Failed to add area feature to area as it doesn't exist"
			)
	})
	public ResponseEntity<String> addAreaFeatureToArea(@PathVariable UUID areaId,
		@PathVariable String areaFeatureId) {
		super.hasPermissionToPut();
		Area area = getAreaFromId(areaId);
		AreaFeature areaFeature = getAreaFeature(areaFeatureId);
		area.addAreaFeature(areaFeature);
		areaRepository.save(area);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}

