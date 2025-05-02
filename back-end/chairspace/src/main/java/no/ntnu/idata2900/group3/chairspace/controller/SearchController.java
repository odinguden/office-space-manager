package no.ntnu.idata2900.group3.chairspace.controller;

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
	//TODO swagger documentation
	@GetMapping("")
	public ResponseEntity<Page<SimpleArea>> doSearch(
		@RequestParam() int page,
		@RequestParam(name = "items-per-page", required = false) Integer itemsPerPage,
		@RequestParam(required = false) Integer capacity,
		@RequestParam(name = "super-area", required = false) UUID superAreaId,
		@RequestParam(name = "area-type", required = false) String areaTypeId,
		@RequestParam(name = "features", required = false) List<String> areaFeatureIds,
		@RequestParam(name = "start-time") LocalDateTime startDateTime,
		@RequestParam(name = "end-time") LocalDateTime endDateTime,
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

		Page<SimpleArea> simpleAreas = areas.map(areaAssembler::toSimpleArea);
		return new ResponseEntity<>(simpleAreas, HttpStatus.OK);
	}
}