package no.ntnu.idata2900.group3.chairspace.controller;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.assembler.AreaAssembler;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleArea;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
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
	 * @param areaService autowired area service.
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
	public ResponseEntity<SimpleArea> get(@PathVariable UUID id) {
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
	public ResponseEntity<Page<SimpleArea>> getAll(
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
	 * Creates a new area, granted one doesn't already exist.
	 *
	 * @param simpleArea a simplified area container to create from
	 * @return 201 CREATED
	 */
	@PostMapping("")
	public ResponseEntity<UUID> post(@RequestBody SimpleArea simpleArea) {
		this.hasPermissionToPost();
		Area area;

		try {
			area = areaAssembler.assembleArea(simpleArea);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		areaService.create(area);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * Updates an existing area.
	 *
	 * @param simpleArea the simple area containing update data. Values not to be updated should be
	 *     null
	 * @return 204 NO CONTENT
	 */
	@PutMapping("")
	public ResponseEntity<String> put(@RequestBody SimpleArea simpleArea) {
		this.hasPermissionToPut();

		Area area;
		try {
			area = areaAssembler.mergeWithExisting(simpleArea.id(), simpleArea);
		} catch (Exception e) {
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
	public ResponseEntity<String> delete(@PathVariable UUID id) {
		this.hasPermissionToDelete();
		areaService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
