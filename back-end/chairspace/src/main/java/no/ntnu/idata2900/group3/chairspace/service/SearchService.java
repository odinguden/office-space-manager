package no.ntnu.idata2900.group3.chairspace.service;

import java.util.List;
import no.ntnu.idata2900.group3.chairspace.dto.SearchDto;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to handle search requests.
 */
@Service
public class SearchService {
	@Autowired
	private AreaService areaService;

	/**
	 * Searches for areas based on the given criteria.
	 *
	 * @param searchDto the search criteria
	 * @return a list of areas that match the criteria
	 */
	public List<AreaDto> search(SearchDto searchDto) {
		Iterable<AreaDto> areas = areaService.searchWithOptionalParams(
			searchDto.getCapacity(),
			searchDto.getAreaType(),
			searchDto.getAreaFeatures(),
			searchDto.getStartDateTime(),
			searchDto.getEndDateTime()
		);

	}



}
