package no.ntnu.idata2900.group3.chairspace.controllers;

import java.util.List;
import no.ntnu.idata2900.group3.chairspace.dto.SearchDto;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaDto;
import no.ntnu.idata2900.group3.chairspace.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	 * @param searchDto the search criteria
	 * @return a response entity containing a list of areas that match the criteria
	 */
	@PostMapping("path")
	public ResponseEntity<List<AreaDto>> doSearch(@RequestBody SearchDto searchDto) {
		List<AreaDto> areas = searchService.search(searchDto);
		return new ResponseEntity<>(areas, HttpStatus.OK);
	}
}
