package no.ntnu.idata2900.group3.chairspace.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.PaginationDto;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaDto;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.exceptions.PageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to handle search requests.
 */
@Service
public class SearchService {
	@Autowired
	private AreaService areaService;
	@Autowired
	private ReservationService reservationService;


	/**
	 * Searches for areas that fit the given criteria.
	 * Page and itemsPerPage are used for pagination and are not optional.
	 * The reset of the parameters are optional
	 * and can be null.
	 * If they are null, they will be ignored in the search.
	 *
	 * @param page the page to get
	 * @param itemsPerPage the number of items per page
	 * @param capacity the minimum capacity of the area
	 * @param superAreaId the super area to search in
	 * @param areaTypeId the type of area to search for
	 * @param areaFeatureIds the features of the area to search for
	 * @param startDateTime the start date and time of the reservation
	 * @param endDateTime the end date and time of the reservation
	 * @param duration the duration of the reservation
	 * @return a list of areas that fit the given criteria
	 * @throws PageNotFoundException if the page is not found
	 */
	public PaginationDto<AreaDto> doSearch(
		int page,
		Integer itemsPerPage,
		Integer capacity,
		UUID superAreaId,
		String areaTypeId,
		List<String> areaFeatureIds,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		Duration duration
	) throws PageNotFoundException {
		// Might be useful to make a method that returns Area objects instead of AreaDto objects
		// This would let save us processing time for searching the database
		List<AreaDto> rawAreaList = reservationService.getAreasThatContainFreeTimeSlot(
			startDateTime,
			endDateTime,
			duration
			);
		List<UUID> freeAreas = new ArrayList<>();
		for (AreaDto areaDto : rawAreaList) {
			freeAreas.add(areaDto.getId());
		}

		Iterable<Area> areas = areaService.searchWithOptionalParams(
			capacity,
			superAreaId,
			areaTypeId,
			areaFeatureIds,
			freeAreas
		);
		List<AreaDto> areaDtos = new ArrayList<>();
		for (Area area : areas) {
			areaDtos.add(new AreaDto(area));
		}

		if (itemsPerPage == null || itemsPerPage <= 0) {
			itemsPerPage = 12;
		}

		return new PaginationDto<>(areaDtos, itemsPerPage, page);
	}
}
