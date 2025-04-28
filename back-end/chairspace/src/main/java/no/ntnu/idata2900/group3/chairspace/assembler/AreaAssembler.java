package no.ntnu.idata2900.group3.chairspace.assembler;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import no.ntnu.idata2900.group3.chairspace.dto.area.SimpleArea;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.AdminCountException;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.service.AreaFeatureService;
import no.ntnu.idata2900.group3.chairspace.service.AreaService;
import no.ntnu.idata2900.group3.chairspace.service.AreaTypeService;
import no.ntnu.idata2900.group3.chairspace.service.UserService;
import org.springframework.stereotype.Component;

/**
 * A utility class that helps convert the Area class to and from its respective DTOs.
 */
@Component
public class AreaAssembler {
	private final AreaService areaService;
	private final AreaTypeService areaTypeService;
	private final AreaFeatureService areaFeatureService;
	private final UserService userService;

	/**
	 * Creates a new Area Assembler.
	 *
	 * @param areaService the area service connected to this assembler
	 * @param areaTypeService the area type service connected to this assembler
	 * @param areaFeatureService the area feature service connected to this assembler
	 * @param userService the user service connected to this assembler
	 */
	public AreaAssembler(
			AreaService areaService,
			AreaTypeService areaTypeService,
			AreaFeatureService areaFeatureService,
			UserService userService
	) {
		this.areaService = areaService;
		this.areaTypeService = areaTypeService;
		this.areaFeatureService = areaFeatureService;
		this.userService = userService;
	}

	/**
	 * Creates an Area based entirely on a SimpleArea.
	 *
	 * @param area the simple area to assemble from
	 * @return the assembled domain area
	 * @throws AdminCountException if the input simple area has no admins
	 * @throws InvalidArgumentCheckedException if the input simple area has fields that are invalid
	 *     for a domain area.
	 */
	public Area assembleArea(SimpleArea area)
		throws AdminCountException, InvalidArgumentCheckedException {
		AreaType areaType = null;
		if (area.areaTypeId() != null && !area.areaTypeId().isBlank()) {
			areaType = areaTypeService.get(area.areaTypeId());
		}

		Area superArea = null;
		if (area.superAreas() != null && !area.superAreas().isEmpty()) {
			superArea = areaService.get(area.superAreas().get(0).id());
		}

		Set<User> administrators = null;
		if (area.administratorIds() != null && !area.administratorIds().isEmpty()) {
			administrators = area.administratorIds()
				.stream()
				.map(userService::get)
				.collect(Collectors.toSet());
		}

		Set<AreaFeature> areaFeatures = null;
		if (area.areaFeatureIds() != null && !area.areaFeatureIds().isEmpty()) {
			areaFeatures = area.areaFeatureIds()
				.stream()
				.map(areaFeatureService::get)
				.collect(Collectors.toSet());
		}

		Area.Builder areaBuilder = new Area.Builder(area.name(), area.capacity(), areaType);

		areaBuilder.description(area.description())
			.superArea(superArea)
			.administrators(administrators)
			.features(areaFeatures)
			.calendarLink(area.calendarLink())
			.reservable(area.reservable())
			.id(area.id());

		return areaBuilder.build();
	}

	/**
	 * Creates an Area based on an existing area and a simple area to layer onto that.
	 *
	 * @param existingId the ID of an existing area
	 * @param area the simple area to layer on top of the existing area
	 * @return the existing area where all non-null values of the simple area are pasted on top
	 * @throws AdminCountException if the input simple area has no admins and a non-null admin list
	 * @throws InvalidArgumentCheckedException if the input simple area has fields that are invalid
	 *     for a domain area.
	 */
	public Area mergeWithExisting(UUID existingId, SimpleArea area)
		throws AdminCountException, InvalidArgumentCheckedException {
		Area existingArea = areaService.get(existingId);
		if (existingArea == null) {
			throw new IllegalArgumentException();
		}

		AreaType areaType = existingArea.getAreaType();
		if (area.areaTypeId() != null && !area.areaTypeId().isBlank()) {
			areaType = areaTypeService.get(area.areaTypeId());
		}

		Area superArea = existingArea.getSuperArea();
		if (area.superAreas() != null && !area.superAreas().isEmpty()) {
			superArea = areaService.get(area.superAreas().get(0).id());
		}

		Set<User> administrators = existingArea.getAreaSpecificAdministrators();
		if (area.administratorIds() != null && !area.administratorIds().isEmpty()) {
			administrators = area.administratorIds()
				.stream()
				.map(userService::get)
				.collect(Collectors.toSet());
		}

		Set<AreaFeature> areaFeatures = existingArea.getFeatures();
		if (area.areaFeatureIds() != null && !area.areaFeatureIds().isEmpty()) {
			areaFeatures = area.areaFeatureIds()
				.stream()
				.map(areaFeatureService::get)
				.collect(Collectors.toSet());
		}

		String name = pickOrElse(area.name(), existingArea.getName());
		String description = pickOrElse(area.description(), existingArea.getDescription());
		Integer capacity = pickOrElse(area.capacity(), existingArea.getCapacity());
		String calendarLink = pickOrElse(area.calendarLink(), existingArea.getCalendarLink());
		boolean reservable = pickOrElse(area.reservable(), existingArea.isReservable());

		Area.Builder areaBuilder = new Area.Builder(name, capacity, areaType);
		areaBuilder.description(description)
			.superArea(superArea)
			.administrators(administrators)
			.features(areaFeatures)
			.calendarLink(calendarLink)
			.reservable(reservable)
			.id(existingId);

		return areaBuilder.build();
	}

	/**
	 * Converts a domain area to a simple area.
	 *
	 * @param area the area to simplify
	 * @return the area represented by a simple area object
	 */
	public SimpleArea toSimpleArea(Area area) {
		return SimpleArea.Builder.fromArea(area).build();

	}

	/**
	 * Returns {@code primary} as long as it is not null, and {@code fallback} otherwise.
	 *
	 * @param primary the primary choice for a value
	 * @param fallback the fallback value to return if the primary choice is null
	 * @return primary if it is not null, fallback otherwise.
	 */
	private <T> T pickOrElse(T primary, T fallback) {
		return primary == null ? fallback : primary;
	}

	/**
	 * Returns {@code primary} as long as it is not null or blank, and {@code fallback} otherwise.
	 *
	 * @param primary the primary choice for a value
	 * @param fallback the fallback value to return if the primary choice is null
	 * @return primary if it is not null, fallback otherwise.
	 */
	private String pickOrElse(String primary, String fallback) {
		return primary == null || primary.isBlank() ? fallback : primary;
	}
}
