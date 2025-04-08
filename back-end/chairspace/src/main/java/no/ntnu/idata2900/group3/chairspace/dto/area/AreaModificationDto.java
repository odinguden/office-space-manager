package no.ntnu.idata2900.group3.chairspace.dto.area;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;

/**
 * A data transfer object used for modification of area objects.
 * Contains all the fields from {@link Area} that can be modified without consequences .
 */
public class AreaModificationDto {
	@JsonProperty
	private UUID id;
	@JsonProperty
	private String name;
	@JsonProperty
	private String description;
	@JsonProperty
	private String calendarLink;
	@JsonProperty
	private Integer capacity;
	@JsonProperty
	private Boolean reservable;
	@JsonProperty
	private String areaType;

	/**
	 * No args constructor.
	 */
	public AreaModificationDto() {
		// Empty constructor for serialization/deserialization
	}

	/**
	 * Returns uuid of the area.
	 *
	 * @return UUID of the area.
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Returns the name of the area.
	 *
	 * @return the name of the area
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description of the area.
	 *
	 * @return returns the description of the area
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the calendar link of the area or null if it doesn't have any.
	 *
	 * @return returns the calendar link of the area
	 */
	public String getCalendarLink() {
		return calendarLink;
	}

	/**
	 * Returns the capacity of the area.
	 *
	 * @return returns the capacity of the area
	 */
	public Integer getCapacity() {
		return capacity;
	}

	/**
	 * Returns true if the area is reservable.
	 *
	 * @return returns true if the area is reservable
	 */
	public Boolean isReservable() {
		return reservable;
	}

	/**
	 * Returns the type of the area.
	 *
	 * @return returns the type of the area
	 */
	public String getAreaType() {
		return areaType;
	}
}
