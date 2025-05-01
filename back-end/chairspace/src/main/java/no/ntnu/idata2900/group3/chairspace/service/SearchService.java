package no.ntnu.idata2900.group3.chairspace.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Service to handle search requests.
 */
@Service
public class SearchService {
	private final AreaService areaService;

	/**
	 * Creates a new search service.
	 *
	 * @param areaService autowired area service
	 */
	public SearchService(AreaService areaService) {
		this.areaService = areaService;
	}

	/**
	 * Searches for areas that fit the given criteria.
	 * Page and itemsPerPage are used for pagination and are not optional.
	 * The reset of the parameters are optional
	 * and can be null.
	 * If they are null, they will be ignored in the search.
	 *
	 * @param page the page of the pagination to get
	 * @param capacity the minimum capacity of the area
	 * @param superAreaId the super area to search in
	 * @param areaTypeId the type of area to search for
	 * @param areaFeatureIds the features of the area to search for
	 * @param startDateTime the start date and time of the reservation
	 * @param endDateTime the end date and time of the reservation
	 * @param duration the duration of the reservation
	 * @return a list of areas that fit the given criteria
	 */
	public Page<Area> doSearch(
		int page,
		Integer capacity,
		UUID superAreaId,
		String areaTypeId,
		List<String> areaFeatureIds,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		Duration duration
	) {
		// Might be useful to make a method that returns Area objects instead of AreaDto objects
		// This would let save us processing time for searching the database
		List<Area> rawAreaList = areaService.getAreasWithFreeGapLike(
			startDateTime,
			endDateTime,
			duration
		);
		List<UUID> freeAreas = rawAreaList.stream().map(Area::getId).toList();

		return areaService.searchWithOptionalParams(
			page,
			capacity,
			superAreaId,
			areaTypeId,
			areaFeatureIds,
			freeAreas
		);
	}
}
