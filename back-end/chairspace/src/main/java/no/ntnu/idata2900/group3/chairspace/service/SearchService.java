package no.ntnu.idata2900.group3.chairspace.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaDto;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service to handle search requests.
 */
@Service
public class SearchService {
	@Autowired
	private AreaService areaService;
	@Autowired
	private AreaTypeService areaTypeService;
	@Autowired
	private AreaFeatureService areaFeatureService;
	@Autowired
	private ReservationService reservationService;


	public List<AreaDto> doSearch(
		Integer capacity,
		UUID superAreaId,
		String areaTypeId,
		List<String> areaFeatureIds,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime
	) {
		AreaType areaType = null;
		if (areaTypeId != null) {
			areaType = areaTypeService.getEntity(areaTypeId);
			if (areaType == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area type not found");
			}
		}

		Area superArea = null;
		if (superAreaId != null) {
			superArea = areaService.getArea(superAreaId);
			if (superArea == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Super area not found");
			}
		}

		// TODO, fix to much nesting
		List<AreaFeature> areaFeatures = new ArrayList<>();
		if (areaFeatureIds != null) {
			for (String areaFeatureId : areaFeatureIds) {
				AreaFeature areaFeature = areaFeatureService.getEntity(areaFeatureId);
				if (areaFeature == null) {
					throw new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Area feature not found"
						);
				}
				areaFeatures.add(areaFeature);
			}
		}

		// Might be useful to make a method that returns Area objects instead of AreaDto objects
		// This would let save us processing time for searching the database
		List<AreaDto> rawAreaList = reservationService.getAreasThatContainFreeTimeSlot(
			startDateTime,
			endDateTime,
			null
		);

		List<Area> freeAreas = new ArrayList<>();
		for (AreaDto areaDto : rawAreaList) {
			freeAreas.add(areaService.getArea(areaDto.getId()));
		}


		Iterable<Area> areas = areaService.searchWithOptionalParams(
			capacity,
			superArea,
			areaType,
			areaFeatures,
			freeAreas
		);
		List<AreaDto> areaDtos = new ArrayList<>();
		for (Area area : areas) {
			areaDtos.add(new AreaDto(area));
		}
		return areaDtos;
	}



}
