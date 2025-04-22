package no.ntnu.idata2900.group3.chairspace.dto.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A record containing all data needed for the creation of a new reservation.
 */
public class ReservationCreationDto {
	@JsonProperty
	protected UUID user;
	@JsonProperty
	protected UUID area;
	@JsonProperty
	protected LocalDateTime startTime;
	@JsonProperty
	protected LocalDateTime endTime;
	@JsonProperty
	protected String comment;

	/**
	 * Gets the id of this reservation's user.
	 *
	 * @return the id of this reservation's user
	 */
	public UUID getUser() {
		return user;
	}

	/**
	 * Gets the reservation's area id.
	 *
	 * @return the reservation's area id
	 */
	public UUID getArea() {
		return area;
	}

	/**
	 * Gets the reservation's start time.
	 *
	 * @return the reservation's start time
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * Gets the reservations's end time.
	 *
	 * @return the reservations's end time
	 */
	public LocalDateTime getEndTime() {
		return endTime;
	}

	/**
	 * Gets the reservation's comment.
	 *
	 * @return the reservation's comment
	 */
	public String getComment() {
		return comment;
	}
}
