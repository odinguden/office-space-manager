package no.ntnu.idata2900.group3.chairspace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaCreationDto;
import no.ntnu.idata2900.group3.chairspace.dto.area.AreaDto;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.AdminCountException;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.repository.AreaFeatureRepository;
import no.ntnu.idata2900.group3.chairspace.repository.AreaRepository;
import no.ntnu.idata2900.group3.chairspace.repository.AreaTypeRepository;
import no.ntnu.idata2900.group3.chairspace.repository.UserRepository;
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
	UserRepository userRepository;
	@Autowired
	AreaTypeRepository areaTypeRepository;
	@Autowired
	AreaFeatureRepository areaFeatureRepository;

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
	 * Creates an area from the given AreaCreationDto.
	 *
	 * @param areaDto The AreaCreationDto object containing the data to create the area.
	 * @return The created Area object.
	 */
	public Area createAreaFromDto(AreaCreationDto areaDto)
		throws AdminCountException, InvalidArgumentCheckedException {
		AreaType areaType = getAreaType(areaDto.getAreaType());
		Area.Builder areaBuilder = new Area.Builder(
			areaDto.getName(),
			areaDto.getCapacity(),
			areaType
		);

		for (UUID id : areaDto.getAdministrators()) {
			areaBuilder.administrator(
				getUser(id)
			);
		}

		areaBuilder.calendarLink(areaDto.getCalendarLink());
		areaBuilder.superArea(getArea(areaDto.getSuperArea()));
		areaBuilder.description(areaDto.getDescription());
		for (String areaFeature : areaDto.getAreaFeatures()) {
			areaBuilder.feature(
				getAreaFeature(areaFeature)
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


	/* ---- Database Methods ---- */

	/**
	 * Builds an area from the given AreaCreationDto and saves it to the database.
	 *
	 * @param areaDto The AreaCreationDto object containing the data to create the area.
	 * @throws InvalidArgumentCheckedException if the areaCreationDto contains invalid arguments
	 * @throws AdminCountException if an area is created without an admin
	 */
	public void saveAreaFromCreationDto(AreaCreationDto areaDto)
		throws AdminCountException, InvalidArgumentCheckedException {
		Area area = createAreaFromDto(areaDto);
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
		// Check if the area exists in the repository
		if (areaRepository.existsById(id)) {
			// Remove the area from the repository
			areaRepository.deleteById(id);
			return true;
		}
		return false;
	}

	/* ---- Non Area Database methods ---- */
	// Only for use with AreaService, not for use with other services

	/**
	 * Returns an area feature based on id.
	 *
	 * @param id string id
	 * @return Area feature from the database
	 */
	private AreaFeature getAreaFeature(String id) {
		return areaFeatureRepository.findById(id).orElse(null);
	}

	/**
	 * Returns a user based on id.
	 * Returns null if the user does not exist in the database.
	 *
	 * @param id UUID of the user
	 * @return User from the database
	 */
	private User getUser(UUID id) {
		return userRepository.findById(id).orElse(null);
	}

	/**
	 * Returns an area type based on id.
	 * Returns null if the area type does not exist in the database.
	 *
	 * @param id string id
	 * @return Area type from the database
	 */
	private AreaType getAreaType(String id) {
		return areaTypeRepository.findById(id).orElse(null);
	}
}
