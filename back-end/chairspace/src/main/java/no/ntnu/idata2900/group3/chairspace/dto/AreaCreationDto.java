package no.ntnu.idata2900.group3.chairspace.dto;

import java.util.List;
import java.util.UUID;

/**
 * DTO to be used when creating Areas.
 * Contains felids for relevant data needed for the construction of a new Area.
 * This dto will only utilize the ID's for relevant entity.
 */
public class AreaCreationDto {
	private List<UUID> administrators;
	private UUID superArea;
	private String areaType;
	private List<String> areaFeatures;
	private int capacity;
	private String calendarLink;
	private String name;
	private String description;
	private boolean reservable;

	/**
	 * Returns the uuid of administrator.
	 *
	 * @return uuid of admin.
	 */
	public List<UUID> getAdministrators() {
		return administrators;
	}

	/**
	 * Returns the ID of the super area.
	 *
	 * @return UUID
	 */
	public UUID getSuperArea() {
		return superArea;
	}

	/**
	 * Returns the name of the area type.
	 *
	 * @return name as string
	 */
	public String getAreaType() {
		return areaType;
	}

	/**
	 * Returns the name of the area features in a list of strings.
	 *
	 * @return names of area features
	 */
	public List<String> getAreaFeatures() {
		return areaFeatures;
	}

	/**
	 * Returns the capacity of the area to be created.
	 *
	 * @return capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Returns calendar link as string.
	 * Will return null if calendar link does not exits
	 *
	 * @return calendar link
	 */
	public String getCalendarLink() {
		return calendarLink;
	}

	/**
	 * Returns name of area.
	 *
	 * @return name of area
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns area description.
	 *
	 * @return area description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns reservable status of area.
	 *
	 * @return reservable status of area
	 */
	public boolean isReservable() {
		return reservable;
	}
}
