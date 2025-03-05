package no.ntnu.idata2900.group3.chairspace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a reservation of an area.
 *
 *
 * @author Odin Lyngsgård
 * @version 0.1
 * @since 0.1
 */
@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID reservationUuid;
	@ManyToOne
	private Area area;
	@ManyToOne
	private User user;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String comment;

	/**
	 * No args constructor for JPA.
	 */
	public Reservation() {}

	/**
	 * Constructor for Reservation.
	 *
	 * @param area Area to reserve
	 * @param account that is reserving the area
	 * @param start start date and time of reservation
	 * @param end end date and time of reservation
	 * @param comment comment for the reservation
	 */
	public Reservation(
		Area area,
		User user,
		LocalDateTime start,
		LocalDateTime end,
		String comment
	) {
		setArea(area);
		setUser(user);
		setStart(start);
		setEnd(end);
		setComment(comment);
	}




	/* ---- Setters ---- */

	/**
	 * Sets the id of the reservation.
	 *
	 * @param id as UUID
	 */
	public void setId(UUID id) {
		this.reservationUuid = id;
	}

	/**
	 * Sets the area of the reservation.
	 *
	 * @param area to reserve
	 */
	private void setArea(Area area) {
		if (area == null) {
			throw new IllegalArgumentException("Area cannot be null");
		}
		this.area = area;
	}

	/**
	 * Sets the user of the reservation.
	 *
	 * @param user that is reserving the area
	 */
	private void setUser(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		this.user = user;
	}

	/**
	 * Sets the start date and time of the reservation.
	 *
	 * @param start date and time of reservation
	 */
	private void setStart(LocalDateTime start) {
		if (start == null) {
			throw new IllegalArgumentException("Start time cannot be null");
		}
		if (start.isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("Cannot create a reservation in the past");
		}
		this.startDateTime = start;
	}

	/**
	 * Sets the end date and time of the reservation.
	 *
	 * @param end date and time of reservation
	 */
	private void setEnd(LocalDateTime end) {
		if (end == null) {
			throw new IllegalArgumentException("End time cannot be null");
		}
		if (end.isBefore(startDateTime)) {
			throw new IllegalArgumentException("End time cannot be before start time");
		}
		this.endDateTime = end;
	}

	/**
	 * Sets the comment for the reservation.
	 *
	 * @param comment for the reservation
	 */
	private void setComment(String comment) {
		if (comment == null || comment.isBlank()) {
			throw new IllegalArgumentException("Comment cannot be blank or null");
		}
		this.comment = comment;
	}

	/* ---- Getters ---- */

	/**
	 * Returns id as UUID.
	 *
	 * @return Id as UUID
	 */
	public UUID getId() {
		return reservationUuid;
	}

	/**
	 * Returns the area of the reservation.
	 *
	 * @return Area to reserve
	 */
	public Area getArea() {
		return area;
	}

	/**
	 * Returns the user of the reservation.
	 *
	 * @return User that is reserving the area
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Returns the start date and time of the reservation.
	 *
	 * @return Start date and time of reservation
	 */
	public LocalDateTime getStart() {
		return startDateTime;
	}

	/**
	 * Returns the end date and time of the reservation.
	 *
	 * @return End date and time of reservation
	 */
	public LocalDateTime getEnd() {
		return endDateTime;
	}

	/**
	 * Returns the comment for the reservation.
	 *
	 * @return Comment for the reservation
	 */
	public String getComment() {
		return comment;
	}
}
