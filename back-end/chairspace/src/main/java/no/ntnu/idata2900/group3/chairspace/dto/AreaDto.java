package no.ntnu.idata2900.group3.chairspace.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import org.apache.commons.lang3.NotImplementedException;

/**
 * A data transfer object used for creation and retrieval of area objects.
 * TODO: Make method that builds area from this dto
 */
public class AreaDto {
	private UUID id;
	private List<UUID> administrators;
	private UUID superArea;
	private AreaType areaType;
	private List<AreaFeature> areaFeatures;
	private int capacity;
	private String calendarLink;
	private String name;
	private String description;
	private boolean reservable;

	public AreaDto(Area area) {
		if (area == null) {
			//TODO: DO somthing
		}

		this.id = area.getId();
		this.capacity = area.getCapacity();
		this.calendarLink = area.getCalendarLink();
		this.name = area.getName();
		this.description = area.getDescription();
		this.reservable = area.isReservable();
		if (area.getSuperArea() != null) {
			this.superArea = area.getSuperArea().getId();
		} else {
			this.superArea = null;
		}
		this.areaType = area.getAreaType();
		this.administrators = new ArrayList<>();

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

	/* ---- Getters ---- */

	/**
	 * Returns the id of the area this DTO represents.
	 *
	 * @return 
	 */
	public UUID getId() {
		return id;
	}

	public List<UUID> getAdministrators() {
		return administrators;
	}

	public UUID getSuperArea() {
		return superArea;
	}

	public AreaType getAreaType() {
		return areaType;
	}

	public List<AreaFeature> getAreaFeatures() {
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

	/**
	 * TODO:
	 *
	 * @return Area based on this object
	 */
	public Area buildArea() {
		throw new NotImplementedException();
	}
}
