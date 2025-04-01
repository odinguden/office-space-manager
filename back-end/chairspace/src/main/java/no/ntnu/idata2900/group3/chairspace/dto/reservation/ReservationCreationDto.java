package no.ntnu.idata2900.group3.chairspace.dto.reservation;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

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

	public UUID getUser() {
		return user;
	}
	public UUID getArea() {
		return area;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public String getComment() {
		return comment;
	}
}
