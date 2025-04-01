package no.ntnu.idata2900.group3.chairspace.dto.reservation;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import no.ntnu.idata2900.group3.chairspace.entity.Reservation;

/**
 * A dto containing relevant information about a reservation.
 */
public class ReservationDto extends ReservationCreationDto {

	@JsonProperty
	private UUID id;

	public ReservationDto() {}

	public ReservationDto(Reservation reservation) {
		this.id = reservation.getId();
		this.user = reservation.getUser().getId();
		this.area = reservation.getArea().getId();
		this.startTime = reservation.getStart();
		this.endTime = reservation.getEnd();
		this.comment = reservation.getComment();
	}

	public UUID getId() {
		return id;
	}
}
