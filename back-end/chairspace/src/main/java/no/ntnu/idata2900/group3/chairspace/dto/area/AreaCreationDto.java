package no.ntnu.idata2900.group3.chairspace.dto.area;

import java.util.List;
import java.util.UUID;

/**
 * DTO to be used when creating Areas.
 * Contains felids for relevant data needed for the construction of a new Area.
 * This dto will only utilize the ID's for relevant entity.
 */
public class AreaCreationDto {
	private List<UUID> administratorIds;
	private UUID superAreaId;
	private String areaTypeId;
	private List<String> areaFeatureIds;
	private int capacity;
	private String calendarLink;
	private String name;
	private String description;
	private boolean reservable;

	/**
	 * No-args constructor.
	 */
	public AreaCreationDto() {
		// Empty constructor for serialization/deserialization
	}

	/**
	 * Returns the uuid of administrator.
	 *
	 * @return uuid of admin.
	 */
	public List<UUID> getAdministratorIds() {
		return administratorIds;
	}

	/**
	 * Returns the ID of the super area.
	 *
	 * @return UUID
	 */
	public UUID getSuperAreaId() {
		return superAreaId;
	}

	/**
	 * Returns the name of the area type.
	 *
	 * @return name as string
	 */
	public String getAreaTypeId() {
		return areaTypeId;
	}

	/**
	 * Returns the name of the area features in a list of strings.
	 *
	 * @return names of area features
	 */
	public List<String> getAreaFeatureIds() {
		return areaFeatureIds;
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
