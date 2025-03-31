package no.ntnu.idata2900.group3.chairspace.dto;

import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.entity.AreaType;

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
	 * Returns the uuid of administrator
	 *
	 * @return uuid of admin.
	 */
	public List<UUID> getAdministrators() {
		return administrators;
	}
	public UUID getSuperArea() {
		return superArea;
	}
	public String getAreaType() {
		return areaType;
	}
	public List<String> getAreaFeatures() {
		return areaFeatures;
	}
	public int getCapacity() {
		return capacity;
	}
	public String getCalendarLink() {
		return calendarLink;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public boolean isReservable() {
		return reservable;
	}

	public Area buildArea() {
		return null;
	}
}
