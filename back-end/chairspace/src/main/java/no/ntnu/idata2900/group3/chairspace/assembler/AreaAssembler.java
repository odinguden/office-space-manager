package no.ntnu.idata2900.group3.chairspace.assembler;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleArea;
import no.ntnu.idata2900.group3.chairspace.dto.SimplePlan;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleReservation;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleReservationList;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.AdminCountException;
import no.ntnu.idata2900.group3.chairspace.exceptions.ElementNotFoundException;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.service.AreaService;
import no.ntnu.idata2900.group3.chairspace.service.PlanService;
import no.ntnu.idata2900.group3.chairspace.service.ReservationService;
import no.ntnu.idata2900.group3.chairspace.service.UserService;
import org.springframework.stereotype.Component;

/**
 * A utility class that helps convert the Area class to and from its respective DTOs.
 */
@Component
public class AreaAssembler {
	private final AreaService areaService;
	private final UserService userService;
	private final ReservationService reservationService;
	private final ReservationAssembler reservationAssembler;
	private final PlanService planService;
	private final PlanAssembler planAssembler;

	/**
	 * Creates a new Area Assembler.
	 *
	 * @param areaService the area service connected to this assembler
	 * @param userService the user service connected to this assembler
	 * @param reservationService the reservation service connected to this assembler
	 * @param reservationAssembler autowired reservation assembler
	 * @param planService autowired plan service
	 * @param planAssembler autowired plan assembler
	 */
	public AreaAssembler(
		AreaService areaService,
		UserService userService,
		ReservationService reservationService,
		ReservationAssembler reservationAssembler,
		PlanService planService,
		PlanAssembler planAssembler
	) {
		this.areaService = areaService;
		this.userService = userService;
		this.reservationService = reservationService;
		this.reservationAssembler = reservationAssembler;
		this.planService = planService;
		this.planAssembler = planAssembler;
	}

	/**
	 * Creates an Area based entirely on a SimpleArea.
	 *
	 * @param area the simple area to assemble from
	 * @return the assembled domain area
	 * @throws AdminCountException if the input simple area has no admins
	 * @throws InvalidArgumentCheckedException if the input simple area has fields that are invalid
	 *     for a domain area.
	 * @throws ElementNotFoundException if any ID in the process refers to a non-existent entity.
	 */
	public Area assembleArea(SimpleArea area)
		throws AdminCountException, InvalidArgumentCheckedException, ElementNotFoundException {
		Area superArea = unpackSuperArea(area);
		Set<User> administrators = unpackAdministrators(area);

		Area.Builder areaBuilder = new Area.Builder(area.name(), area.capacity(), area.areaType());

		areaBuilder.description(area.description())
			.superArea(superArea)
			.administrators(administrators)
			.features(area.areaFeatures())
			.calendarLink(area.calendarLink())
			.reservable(area.reservable())
			.id(area.id())
			.isPlanControlled(area.isPlanControlled());

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
	 * @throws ElementNotFoundException if any ID in the process refers to a non-existent entity.
	 */
	public Area mergeWithExisting(UUID existingId, SimpleArea area)
		throws AdminCountException, InvalidArgumentCheckedException, ElementNotFoundException {
		Area existingArea = areaService.get(existingId);
		if (existingArea == null) {
			throw ElementNotFoundException.AREA_NOT_FOUND;
		}

		Area superArea = pickOrElse(unpackSuperArea(area), existingArea.getSuperArea());

		Set<User> administrators = pickOrElse(
			unpackAdministrators(area),
			existingArea.getAreaSpecificAdministrators()
		);

		String name = pickOrElse(area.name(), existingArea.getName());
		String description = pickOrElse(area.description(), existingArea.getDescription());
		Integer capacity = pickOrElse(area.capacity(), existingArea.getCapacity());
		String calendarLink = pickOrElse(area.calendarLink(), existingArea.getCalendarLink());
		boolean reservable = pickOrElse(area.reservable(), existingArea.isReservable());
		AreaType areaType = pickOrElse(area.areaType(), existingArea.getAreaType());
		Set<AreaFeature> areaFeatures = pickOrElse(area.areaFeatures(), existingArea.getFeatures());

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
		SimpleArea.Builder builder = SimpleArea.Builder.fromArea(area);
		builder.planControlled(area.isPlanControlled());
		if (area.isPlanControlled()) {
			List<SimplePlan> simplePlans = planService.getPlansByArea(area.getId())
				.stream()
				.map(planAssembler::toSimple)
				.toList();
			builder.simplePlans(simplePlans);
		}

		return builder.build();
	}

	/**
	 * Converts a domain area to a simple area and includes the area's reservations.
	 *
	 * @param area the area to simplify
	 * @param start the time start of the search
	 * @param end the time end of the search
	 * @return the area represented by a simple area object
	 */
	public SimpleArea toSimpleAreaWithReservations(
		Area area,
		LocalDateTime start,
		LocalDateTime end
	) {
		List<SimpleReservation> reservations = getReservationsForArea(area.getId(), start, end)
			.stream()
			.map(reservationAssembler::toSimple)
			.toList();

		SimpleReservationList simpleReservationList = new SimpleReservationList(
			start,
			end,
			reservations
		);

		SimpleArea.Builder builder = SimpleArea.Builder.fromArea(area);
		builder.reservations(simpleReservationList);
		builder.planControlled(area.isPlanControlled());

		if (area.isPlanControlled()) {
			List<SimplePlan> simplePlans = planService.getPlansByArea(area.getId())
				.stream()
				.map(planAssembler::toSimple)
				.toList();
			builder.simplePlans(simplePlans);
		}

		return builder.build();
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

	private Area unpackSuperArea(SimpleArea simpleArea) throws ElementNotFoundException {
		boolean doesAreaExist = simpleArea != null
			&& simpleArea.superAreas() != null
			&& !simpleArea.superAreas().isEmpty()
			&& simpleArea.superAreas().get(0) != null;

		Area area = null;

		if (doesAreaExist) {
			area = areaService.get(simpleArea.superAreas().get(0).id());
			if (area == null) {
				throw ElementNotFoundException.AREA_NOT_FOUND;
			}
		}

		return area;
	}

	private Set<User> unpackAdministrators(SimpleArea simpleArea) throws ElementNotFoundException {
		Set<User> admins = new HashSet<>();

		if (simpleArea != null && simpleArea.administratorIds() != null) {
			for (UUID id : simpleArea.administratorIds()) {
				User user = userService.get(id);

				if (user == null) {
					throw ElementNotFoundException.USER_NOT_FOUND;
				}

				admins.add(user);
			}
		}

		return admins;
	}

	private List<Reservation> getReservationsForArea(
		UUID areaId, LocalDateTime start, LocalDateTime end
	) {
		return reservationService.getReservationsForAreaBetween(areaId, start, end);
	}
}
