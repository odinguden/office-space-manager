package no.ntnu.idata2900.group3.chairspace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.PaginationDto;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaCreationDto;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaDto;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaModificationDto;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.AdminCountException;
import no.ntnu.idata2900.group3.chairspace.exceptions.ElementNotFoundException;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.PageNotFoundException;
import no.ntnu.idata2900.group3.chairspace.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing and interacting with areas.
 */
@Service
public class AreaService {
	@Autowired
	AreaRepository areaRepository;
	@Autowired
	UserService userService;
	@Autowired
	AreaTypeService areaTypeService;
	@Autowired
	AreaFeatureService areaFeatureService;

	/**
	 * No-args constructor for AreaService.
	 */
	public AreaService() {
		// No-args constructor
	}

	/* ---- Get x ---- */

	/**
	 * Get all areas. Returns an iterable of all areas in the repository.
	 *
	 * @return Iterable of all areas in the repository
	 */
	public Iterable<Area> getAreas() {
		// Get all areas from the repository
		return areaRepository.findAll();
	}

	/**
	 * Get area by id. Returns null if area is not found.
	 *
	 * @param id UUID of the area to get
	 * @return Area with the given id, or null if not found
	 */
	public Area getArea(UUID id) {
		// Get area by id from the repository
		return areaRepository.findById(id).orElse(null);
	}

	/* ---- DTO methods  ---- */


	/**
	 * Creates an area based on the data contained within a area dto.
	 *
	 * @param areaDto dto to create area from
	 * @return Created area object
	 * @throws AdminCountException no administrators are provided
	 * @throws InvalidArgumentCheckedException If any of the arguments provided in the
	 *     Dto are invalid for the creation of the area
	 * @throws ElementNotFoundException if areaType, areaFeature or superArea is not found
	 *     in the database
	 */
	public Area createAreaFromDto(AreaCreationDto areaDto)
		throws AdminCountException, InvalidArgumentCheckedException, ElementNotFoundException {
		AreaType areaType = areaTypeService.getEntity(areaDto.getAreaTypeId());
		if (areaType == null) {
			throw ElementNotFoundException.areaTypeNotFoundException;
		}
		Area.Builder areaBuilder = new Area.Builder(
			areaDto.getName(),
			areaDto.getCapacity(),
			areaType
		);

		for (UUID id : areaDto.getAdministratorIds()) {
			User user = userService.getEntity(id);
			if (user == null) {
				throw ElementNotFoundException.userNotFoundException;
			}
			areaBuilder.administrator(user);
		}

		areaBuilder.calendarLink(areaDto.getCalendarLink());
		if (areaDto.getSuperAreaId() != null) {
			Area area = getArea(areaDto.getSuperAreaId());
			if (area == null) {
				throw ElementNotFoundException.areaNotFoundException;
			}
			areaBuilder.superArea(area);
		}
		areaBuilder.description(areaDto.getDescription());
		for (String areaFeature : areaDto.getAreaFeatureIds()) {
			areaBuilder.feature(
				areaFeatureService.getEntity(areaFeature)
			);
		}
		return areaBuilder.build();
	}

	/**
	 * Creates a list of area DTOs from the areas in the repository.
	 *
	 * @return List of AreaDto objects representing the areas in the repository
	 */
	public List<AreaDto> getAreasAsDto() {
		// Get all areas from the repository
		Iterable<Area> areas = areaRepository.findAll();
		List<AreaDto> areaDtos = new ArrayList<>();
		for (Area area : areas) {
			areaDtos.add(new AreaDto(area));
		}
		return areaDtos;
	}

	/**
	 * Returns areas in a pagination dto.
	 *
	 * @param page the page the user is requesting
	 * @param itemsPerPage the number of items contained by each page of the pagination
	 * @return PaginationDto containing 12 areaDto's
	 * @throws PageNotFoundException if a invalid page is requested
	 */
	public PaginationDto<AreaDto> getAreaPagination(int page, int itemsPerPage)
		throws PageNotFoundException {
		return new PaginationDto<>(getAreasAsDto(), itemsPerPage, page);
	}


	/* ---- Database Methods ---- */

	/**
	 * Adds a single area feature to an area.
	 *
	 * @param areaId id of the area
	 * @param featureId id of the feature
	 * @throws ElementNotFoundException if either the feature or the area can not be
	 *     found by id in the database
	 */
	public void addFeatureToArea(UUID areaId, String featureId) throws ElementNotFoundException {
		Area area = getArea(areaId);
		if (area == null) {
			throw ElementNotFoundException.areaNotFoundException;
		}
		AreaFeature areaFeature = areaFeatureService.getEntity(featureId);
		if (areaFeature == null) {
			throw ElementNotFoundException.areaFeatureNotFoundException;
		}
		area.addAreaFeature(areaFeature);
		areaRepository.save(area);
	}

	/**
	 * Removes an areaFeature from an area.
	 *
	 * @param areaId id of the area
	 * @param featureId id of the feature
	 * @throws ElementNotFoundException if either the feature or the area can not be
	 *     found by id in the database
	 */
	public void removeFeatureFromArea(UUID areaId, String featureId)
		throws ElementNotFoundException {
		Area area = getArea(areaId);
		if (area == null) {
			throw ElementNotFoundException.areaNotFoundException;
		}
		AreaFeature areaFeature = areaFeatureService.getEntity(featureId);
		if (areaFeature == null) {
			throw ElementNotFoundException.areaFeatureNotFoundException;
		}
		area.removeAreaFeature(areaFeature);
		areaRepository.save(area);
	}

	/**
	 * Replaces super area of a given area.
	 * Is also used to add a super area if the area's super area is null
	 *
	 * @param areaId id of area that is going to get is's super area replaced
	 * @param superAreaId id of new super area
	 * @throws ElementNotFoundException if either area or new super area can not
	 *     be found in the database
	 * @throws InvalidArgumentCheckedException if new super area is not a valid super area
	 * @throws AdminCountException the action will leave area without any administrators of it's own
	 */
	public void setSuperArea(UUID areaId, UUID superAreaId)
		throws ElementNotFoundException, InvalidArgumentCheckedException, AdminCountException {
		Area area = getArea(areaId);
		if (area == null) {
			throw ElementNotFoundException.areaNotFoundException;
		}
		Area superArea = getArea(superAreaId);
		if (superArea == null) {
			throw ElementNotFoundException.areaNotFoundException;
		}
		area.setSuperArea(superArea);
		areaRepository.save(area);
	}

	/**
	 * Removes a super area from an area.
	 *
	 * @param areaId id of the area to remove super area from
	 * @throws ElementNotFoundException if area can not be found in the database
	 * @throws AdminCountException if area has no admins of it's own
	 */
	public void removeSuperArea(UUID areaId) throws ElementNotFoundException, AdminCountException {
		Area area = getArea(areaId);
		if (area == null) {
			throw ElementNotFoundException.areaNotFoundException;
		}
		area.removeSuperArea();
		areaRepository.save(area);
	}

	/**
	 * Builds an area from the given AreaCreationDto and saves it to the database.
	 *
	 * @param areaDto The AreaCreationDto object containing the data to create the area.
	 * @throws InvalidArgumentCheckedException if the areaCreationDto contains invalid arguments
	 * @throws AdminCountException if an area is created without an admin
	 * @throws ElementNotFoundException if superArea, AreaType or AreaFeature
	 *     can not be found in the database
	 */
	public void saveAreaFromCreationDto(AreaCreationDto areaDto)
		throws AdminCountException, InvalidArgumentCheckedException, ElementNotFoundException {
		Area area = createAreaFromDto(areaDto);
		areaRepository.save(area);
	}

	/**
	 * Updates an area to fit the new data in the areaModificationDto.
	 *
	 * @param areaDto Dto that includes the data that needs updated in an area
	 * @throws InvalidArgumentCheckedException If any of the arguments in the dto are illegal
	 * @throws ElementNotFoundException If area with matching id to areaModificationDto
	 *     cannot be found
	 */
	public void putArea(AreaModificationDto areaDto)
		throws InvalidArgumentCheckedException, ElementNotFoundException {
		Area area = getArea(areaDto.getId());
		if (area == null) {
			throw ElementNotFoundException.areaNotFoundException;
		}
		if (areaDto.getName() != null) {
			area.updateName(areaDto.getName());
		}
		// no check as this can be set to null
		area.updateDescription(areaDto.getDescription());
		if (areaDto.isReservable() != null) {
			area.setReservable(areaDto.isReservable());
		}
		if (areaDto.getCapacity() != null) {
			area.updateCapacity(areaDto.getCapacity());
		}
		if (areaDto.getAreaType() != null) {
			AreaType areaType = areaTypeService.getEntity(areaDto.getAreaType());
			if (areaType == null) {
				// A lot of indentation but seems like a waste to put all this in it's own method
				throw ElementNotFoundException.areaTypeNotFoundException;
			}
			area.updateAreaType(areaType);
		}
		if (areaDto.getCalendarLink() != null) {
			area.updateCalendarLink(areaDto.getCalendarLink());
		}
		areaRepository.save(area);
	}

	/**
	 * Remove an area based on id.
	 * Returns true if the area was removed successfully, false otherwise.
	 *
	 * @param id UUID of the area to remove
	 * @return boolean indicating success of removal
	 */
	public boolean removeArea(UUID id) {
		boolean exist = areaRepository.existsById(id);
		if (exist) {
			areaRepository.deleteById(id);
		}
		return exist;
	}
}
