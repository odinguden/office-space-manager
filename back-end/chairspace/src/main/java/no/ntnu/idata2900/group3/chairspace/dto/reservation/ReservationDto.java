package no.ntnu.idata2900.group3.chairspace.dto.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;

/**
 * A dto containing relevant information about a reservation.
 */
public class ReservationDto extends ReservationCreationDto {

	@JsonProperty
	private UUID id;

	/**
	 * No-args constructor.
	 */
	public ReservationDto() {}

	/**
	 * Creates a new reservation DTO based on an existing reservation.
	 *
	 * @param reservation a DTO constructed from an existing reservation
	 */
	public ReservationDto(Reservation reservation) {
		this.id = reservation.getId();
		this.user = reservation.getUser().getId();
		this.area = reservation.getArea().getId();
		this.startTime = reservation.getStart();
		this.endTime = reservation.getEnd();
		this.comment = reservation.getComment();
	}

	/**
	 * Gets the reservation's id.
	 *
	 * @return the reservation's id
	 */
	public UUID getId() {
		return id;
	}
}
