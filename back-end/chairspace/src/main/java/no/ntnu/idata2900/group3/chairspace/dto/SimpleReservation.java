package no.ntnu.idata2900.group3.chairspace.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;

/**
 * A simple representation of {@link Reservation}.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimpleReservation(
	UUID id,
	UUID areaId,
	UUID userId,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime startTime,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime endTime,
	String comment
) {
	/**
	 * A builder for {@link SimpleReservations}.
	 */
	public static class Builder {
		private UUID id;
		private UUID areaId;
		private UUID userId;
		private LocalDateTime startTime;
		private LocalDateTime endTime;
		private String comment;

		/**
		 * Constructs a new simple reservation from this builder.
		 *
		 * @return a new simple reservation from this builder
		 */
		public SimpleReservation build() {
			return new SimpleReservation(
				this.id,
				this.areaId,
				this.userId,
				this.startTime,
				this.endTime,
				this.comment
			);
		}

		/**
		 * Initializes a builder based on an existing {@link Reservation}.
		 *
		 * @param reservation the existing reservation to create a builder from
		 * @return a builder based on the existing reservation
		 */
		public static final Builder fromReservation(Reservation reservation) {
			return new Builder()
				.id(reservation.getId())
				.areaId(reservation.getArea().getId())
				.userId(reservation.getUser().getId())
				.startTime(reservation.getStart())
				.endTime(reservation.getEnd())
				.comment(reservation.getComment());
		}

		/**
		 * Sets the id of this builder.
		 *
		 * @param id the id to set
		 * @return this builder
		 */
		public Builder id(UUID id) {
			this.id = id;
			return this;
		}

		/**
		 * Sets the areaId of this builder.
		 *
		 * @param areaId the areaId to set
		 * @return this builder
		 */
		public Builder areaId(UUID areaId) {
			this.areaId = areaId;
			return this;
		}

		/**
		 * Sets the userId of this builder.
		 *
		 * @param userId the userId to set
		 * @return this builder
		 */
		public Builder userId(UUID userId) {
			this.userId = userId;
			return this;
		}

		/**
		 * Sets the startTime of this builder.
		 *
		 * @param startTime the startTime to set
		 * @return this builder
		 */
		public Builder startTime(LocalDateTime startTime) {
			this.startTime = startTime;
			return this;
		}

		/**
		 * Sets the endTime of this builder.
		 *
		 * @param endTime the endTime to set
		 * @return this builder
		 */
		public Builder endTime(LocalDateTime endTime) {
			this.endTime = endTime;
			return this;
		}

		/**
		 * Sets the comment of this builder.
		 *
		 * @param comment the comment to set
		 * @return this builder
		 */
		public Builder comment(String comment) {
			this.comment = comment;
			return this;
		}
	}
}
