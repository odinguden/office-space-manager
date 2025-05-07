package no.ntnu.idata2900.group3.chairspace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.assembler.AreaAssembler;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleArea;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.service.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * Controller to handle search requests.
 */
@CrossOrigin(origins = "$frontend.url")
@RestController
@RequestMapping("/search")
public class SearchController {
	private final SearchService searchService;
	private final AreaAssembler areaAssembler;

	/**
	 * Creates a new search controller.
	 *
	 * @param searchService autowired search service
	 * @param areaAssembler autowired area assembler
	 */
	public SearchController(SearchService searchService, AreaAssembler areaAssembler) {
		this.searchService = searchService;
		this.areaAssembler = areaAssembler;
	}

	/**
	 * Handles GET requests to search for areas based on various parameters.
	 * The parameters page and itemsPerPage are required for pagination.
	 * The other parameters are optional and can be used to filter the search results.
	 *
	 * @param page the page number to retrieve (required)
	 * @param itemsPerPage the number of items per page (required)
	 * @param capacity the capacity of the area (optional)
	 * @param superAreaId the ID of the super area (optional)
	 * @param areaTypeId the ID of the area type (optional)
	 * @param areaFeatureIds the list of area feature IDs (optional)
	 * @param startDateTime the start date and time for the search (optional)
	 * @param endDateTime the end date and time for the search (optional)
	 * @param duration the duration for the search (optional)
	 * @return a ResponseEntity containing a PaginationDto with the search results
	 */
	@GetMapping("")
	@Operation(
		summary = "Does a search, and returns the marching areas in a pagination",
		description = "Searches based on several parameters."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Search was preformed "
			)
	})
	public ResponseEntity<Page<SimpleArea>> doSearch(
		@Parameter(description = "the page number to retrieve (required)")
		@RequestParam() int page,
		@Parameter(description = "the number of items per page (required)")
		@RequestParam(name = "items-per-page", required = false) Integer itemsPerPage,
		@Parameter(description = "the capacity of the area (optional)")
		@RequestParam(required = false) Integer capacity,
		@Parameter(description = "the ID of the super area (optional)")
		@RequestParam(name = "super-area", required = false) UUID superAreaId,
		@Parameter(description = "the ID of the area type (optional)")
		@RequestParam(name = "area-type", required = false) String areaTypeId,
		@Parameter(description = "the list of area feature IDs (optional)")
		@RequestParam(name = "features", required = false) List<String> areaFeatureIds,
		@Parameter(description = "the start date and time for the search (optional)")
		@RequestParam(name = "start-time") LocalDateTime startDateTime,
		@Parameter(description = "the end date and time for the search (optional)")
		@RequestParam(name = "end-time") LocalDateTime endDateTime,
		@Parameter(description = "the duration for the search (optional)")
		@RequestParam() Duration duration
	) {
		Page<Area> areas = searchService.doSearch(
				page,
				capacity,
				superAreaId,
				areaTypeId,
				areaFeatureIds,
				startDateTime,
				endDateTime,
				duration
		);

		Page<SimpleArea> simpleAreas = areas.map(area ->
			areaAssembler.toSimpleAreaWithReservations(area, startDateTime, endDateTime)
		);
		return new ResponseEntity<>(simpleAreas, HttpStatus.OK);
	}
}