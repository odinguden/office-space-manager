package no.ntnu.idata2900.group3.chairspace.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaDto;
import no.ntnu.idata2900.group3.chairspace.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private SearchService searchService;

	/**
	 * Searches for areas based on the given criteria.
	 *
	 * @param capacity minimum capacity of the area
	 * @param superAreaId ID of the super area
	 * @param areaTypeId ID of the area type
	 * @param areaFeatureIds list of area feature IDs
	 * @param startDateTime start date and time for the search
	 * @param endDateTime end date and time for the search
	 * @return a list of areas that match the criteria
	 */
	@GetMapping("")
	public ResponseEntity<List<AreaDto>> doSearch(
		@RequestParam(required = false) Integer capacity,
		@RequestParam(required = false) UUID superAreaId,
		@RequestParam(required = false) String areaTypeId,
		@RequestParam(required = false) List<String> areaFeatureIds,
		@RequestParam(required = false) LocalDateTime startDateTime,
		@RequestParam(required = false) LocalDateTime endDateTime
	) {
		List<AreaDto> areas = searchService.doSearch(
				capacity,
				superAreaId,
				areaTypeId,
				areaFeatureIds,
				startDateTime,
				endDateTime
		);
		return new ResponseEntity<>(areas, HttpStatus.OK);
	}
}
