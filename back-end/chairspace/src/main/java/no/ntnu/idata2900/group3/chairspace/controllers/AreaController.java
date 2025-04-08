package no.ntnu.idata2900.group3.chairspace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.PaginationDto;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaCreationDto;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaDto;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaModificationDto;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.exceptions.AdminCountException;
import no.ntnu.idata2900.group3.chairspace.exceptions.ElementNotFoundException;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.PageNotFoundException;
import no.ntnu.idata2900.group3.chairspace.service.AreaService;
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
	private AreaService areaService;

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
	 * @throws ResponseStatusException code 404 if the area feature included
	 *     in the dto does not exist
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
		// TODO: Should make a checked exception for if a param is not found in the database.
		try {
			areaService.saveAreaFromCreationDto(areaDto);
		} catch (AdminCountException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentCheckedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * Returns area with the given id.
	 *
	 * @param id uuid of area
	 * @return area
	 * @throws ResponseStatus exception code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatus exception code 403 forbidden if the authorization
	 *     included is not sufficient to get area
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
		Area area = areaService.getArea(id);
		if (area == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		AreaDto areaDto = new AreaDto(area);
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
	@GetMapping("/all")
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
	public ResponseEntity<List<AreaDto>> getAreas() {
		hasPermissionToGetAll();
		List<AreaDto> areas = areaService.getAreasAsDto();
		return new ResponseEntity<>(areas, HttpStatus.OK);
	}

	/**
	 * Returns all areas from the database as area DTO's.
	 *
	 * @param page the requested page
	 * @return all areas in the database in a pagination
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization
	 *     included is not sufficient to get all areas
	 * @throws ResponseStatusException code 400 if the requested page does not exist
	 */
	@GetMapping("/{page}")
	@Operation(
		summary = "Gets all areas in a pagination",
		description = "Gets all areas in the repository in a pagination containing 12 elements"
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
			),
		@ApiResponse(
			responseCode = "400",
			description = "The requested page does not exist"
			)
	})
	public ResponseEntity<PaginationDto<AreaDto>> getAreasInPagination(@PathVariable int page) {
		hasPermissionToGetAll();
		PaginationDto<AreaDto> areaPagination;
		try {
			areaPagination = areaService.getAreaPagination(page, 12);
		} catch (PageNotFoundException e) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"The requested page does not exit"
			);
		}
		return new ResponseEntity<>(areaPagination, HttpStatus.OK);
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
	 * @throws ResponseStatusException 404 not found if new area type cannot be found
	 * @throws ResponseStatusException 400 bad request if the user tries to change a value to 
	 *     a invalid value
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
			description = "Bad request if not able to update"
				+ " area with the information contained in the dto"
			),
		@ApiResponse(
			responseCode = "404",
			description = "If the area that is trying to be updated can not be "
			+ "found in the database"
			),
		@ApiResponse(
			responseCode = "404",
			description = "If the new area type cannot be found in the database"
			)
	})
	public ResponseEntity<String> putArea(@RequestBody AreaModificationDto areaDto) {
		super.hasPermissionToPut();

		try {
			areaService.putArea(areaDto);
		} catch (InvalidArgumentCheckedException e) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				e.getMessage()
			);
		} catch (ElementNotFoundException e) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				e.getMessage()
			);
		}

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
		if (areaService.removeArea(id)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		throw new ResponseStatusException(
			HttpStatus.NOT_FOUND,
			"Could not find area with matching id to delete"
		);
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
		try {
			areaService.addFeatureToArea(areaId, areaFeatureId);
		} catch (ElementNotFoundException e) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				e.getMessage()
			);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Removes an area feature from an area.
	 *
	 * @param areaId id of the area to remove the feature from
	 * @param areaFeatureId id of the area feature to remove from the area
	 * @return response entity with status 204 no content
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization included with the
	 *      request has insufficient permissions to update entities.
	 * @throws ResponseStatusException code 404 not found upon attempting to update an area that
	 *      does not exist
	 * @throws ResponseStatusException code 404 not found upon attempting to remove an
	 *     areaFeature that does not exist
	 */
	//Made this a put mapping since it modifies the area object
	// and is not a delete operation. It is not a delete operation since the area feature
	@PutMapping("/removeAreaFeature/{areaId}/{areaFeatureId}")
	@Operation(
		summary = "Removes an area feature from an area",
		description = "Attempts to remove an area feature from an area"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Successfully removed area feature from area"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to remove area features from areas"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to remove area features from areas"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Failed to remove area feature from area as it doesn't exist"
			)
	})
	public ResponseEntity<String> removeAreaFeatureFromArea(@PathVariable UUID areaId,
		@PathVariable String areaFeatureId) {
		super.hasPermissionToPut();
		try {
			areaService.removeFeatureFromArea(areaId, areaFeatureId);
		} catch (ElementNotFoundException e) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				e.getMessage()
			);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Replaces the super area of an area.
	 * This is a put mapping since it modifies the area object.
	 *
	 * @param areaId id of the area to replace the super area of
	 * @param superAreaId id of the new super area to set
	 * @return response entity with status 204 no content
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization included with the
	 * 	   request has insufficient permissions to update entities.
	 * @throws ResponseStatusException code 404 not found upon attempting to update an area that
	 * 	   does not exist
	 * @throws ResponseStatusException code 404 not found upon attempting to set a super area that
	 * 	   does not exist
	 * @throws ResponseStatusException code 400 bad request if the super area is not valid
	 * @throws ResponseStatusException code 400 bad request if the super area has no administrators
	 */
	@PutMapping("/replaceSuperArea/{areaId}/{superAreaId}")
	@Operation(
		summary = "Replaces the super area of an area",
		description = "Attempts to replace the super area of an area"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Successfully replaced the super area of the area"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to replace super areas of areas"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to replace super areas of areas"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Failed to replace super area of area as it doesn't exist"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Failed to replace super area of area as the super area doesn't exist"
			),
		@ApiResponse(
			responseCode = "400",
			description = "Failed to replace super area of area as the super area is not valid"
			),
		@ApiResponse(
			responseCode = "400",
			description = "Failed to replace super area of area as the super area has no "
				+ "administrators"
			)
	})
	public ResponseEntity<String> replaceSuperArea(@PathVariable UUID areaId,
		@PathVariable UUID superAreaId) {
		super.hasPermissionToPut();
		try {
			areaService.replaceSuperArea(areaId, superAreaId);
		} catch (ElementNotFoundException e) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				e.getMessage()
			);
		} catch (InvalidArgumentCheckedException | AdminCountException e) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				e.getMessage()
			);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Removes the super area of an area.
	 * This is a put mapping since it modifies the area object.
	 *
	 * @param areaId id of the area to remove the super area from
	 * @return response entity with status 204 no content
	 * @throws ResponseStatusException code 401 unauthorized if the request lacks authorization
	 * @throws ResponseStatusException code 403 forbidden if the authorization included with the
	 *      request has insufficient permissions to update entities.
	 * @throws ResponseStatusException code 404 not found upon attempting to update an area that
	 *      does not exist
	 */
	@PutMapping("removesuperarea/{areaId}")
	@Operation(
		summary = "Removes the super area of an area",
		description = "Attempts to remove the super area of an area"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Successfully removed the super area of the area"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to remove super areas of areas"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to remove super areas of areas"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Failed to remove super area of area as it doesn't exist"
			),
		@ApiResponse(
			responseCode = "400",
			description = "Failed to remove super area of area as"
				+ " the area has no admins of it's own"
			),
	})
	public ResponseEntity<String> removeSuperArea(@PathVariable UUID areaId) {
		super.hasPermissionToPut();
		try {
			areaService.removeSuperArea(areaId);
		} catch (ElementNotFoundException e) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				e.getMessage()
			);
		} catch (AdminCountException e) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				e.getMessage()
			);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}

