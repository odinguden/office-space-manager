package no.ntnu.idata2900.group3.chairspace.dto.area;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * A data transfer object used for retrieval of area objects.
 */
public class AreaDto {
	private UUID id;
	private List<UUID> administrators;
	private List<SimpleSuperAreaDto> superAreas;
	private AreaType areaType;
	private List<AreaFeature> areaFeatures;
	private int capacity;
	private String calendarLink;
	private String name;
	private String description;
	private boolean reservable;

	/**
	 * No args constructor.
	 */
	public AreaDto(){}

	/**
	 * Creates a single instance of an areaDto based on an exiting areaDTO.
	 *
	 * @param area the area to use as base
	 */
	public AreaDto(Area area) {
		if (area == null) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND
			);
		}

		this.id = area.getId();
		this.capacity = area.getCapacity();
		this.calendarLink = area.getCalendarLink();
		this.name = area.getName();
		this.description = area.getDescription();
		this.reservable = area.isReservable();
		this.areaType = area.getAreaType();
		this.administrators = new ArrayList<>();

		setSuperAreas(area);

		Set<User> admins = area.getAdministrators();
		for (User user : admins) {
			this.administrators.add(user.getId());
		}
		Iterator<AreaFeature> it = area.getFeatures();
		this.areaFeatures = new ArrayList<>();
		while (it.hasNext()) {
			areaFeatures.add(it.next());
		}


	}

	private void setSuperAreas(Area area) {
		superAreas = new ArrayList<>();

		Area nextSuper = area.getSuperArea();

		while (nextSuper != null) {
			superAreas.add(
				new SimpleSuperAreaDto(nextSuper)
			);
			nextSuper = nextSuper.getSuperArea();
		}
	}

	/* ---- Getters ---- */

	/**
	 * Returns the id of the area this DTO represents.
	 *
	 * @return uuid
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Returns a list containing the UUID's of the users administering the area this dto represents.
	 *
	 * @return list of UUID's
	 */
	public List<UUID> getAdministrators() {
		return administrators;
	}

	/**
	 * Returns a list containing simpleSuperAreaDto's for all the super areas of this areaDTO.
	 *
	 * @return List containing SimpleSuperAreaDTO's
	 */
	public List<SimpleSuperAreaDto> getSuperAreas() {
		return superAreas;
	}

	/**
	 * Returns area type of area.
	 * Will return full area type object containing name and description.
	 *
	 * @return area type
	 */
	public AreaType getAreaType() {
		return areaType;
	}

	/**
	 * Returns list of area features of the area this DTO represents.
	 * Returns full area feature object containing name and description.
	 *
	 * @return list of area features
	 */
	public List<AreaFeature> getAreaFeatures() {
		return areaFeatures;
	}

	/**
	 * Returns the capacity of the area this DTO represents.
	 *
	 * @return capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Returns calendar link of the area this area represents.
	 * Will be null if no calendar link exits.
	 *
	 * @return calendar link.
	 */
	public String getCalendarLink() {
		return calendarLink;
	}

	/**
	 * Returns the name string of the area this DTO represents.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description of the area this DTO represents.
	 *
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the reservable status of this area.
	 *
	 * @return reservable status
	 */
	public boolean isReservable() {
		return reservable;
	}

	public UUID getIdOfSuperArea() {
		UUID superId = null;
		if (!superAreas.isEmpty()) {
			superId = superAreas.get(0).getId();
		}
		return superId;
	}
}
