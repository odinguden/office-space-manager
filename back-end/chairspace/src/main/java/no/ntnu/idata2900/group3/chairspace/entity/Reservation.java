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
 * Contains the area, user, start and end date and time and a comment.
 *
 * <p>
 * Is used by the {@link Area} and {@link User} classes to represent reservations.
 *
 * <p>
 * This class will automatically add itself to the area's and user's reservation list when created.
 *
 *
 * @author Odin Lyngsgård
 * @version 0.1
 * @since 0.1
 * @see Area
 * @see User
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
	 * Initializes the area, user, start and end date and time and a comment for the reservation.
	 *
	 * <p>
	 * Also adds the reservation to the area and user's reservations list.
	 *
	 * @param area Area to reserve
	 * @param user that is reserving the area
	 * @param start start date and time of reservation
	 * @param end end date and time of reservation
	 * @param comment comment for the reservation
	 * @throws IllegalArgumentException if any of the parameters are null or blank
	 * @throws IllegalStateException if the area is not free for the specified timespan
	 * @throws IllegalStateException if the end time is before the start time
	 * @throws IllegalStateException if the start time is before the current time
	 */
	public Reservation(
		Area area,
		User user,
		LocalDateTime start,
		LocalDateTime end,
		String comment
	) throws IllegalArgumentException, IllegalStateException {
		setUser(user);
		setStart(start);
		setEnd(end);
		setComment(comment);
		// Area needs to be set last. This is to ensure that the reservation is added to the
		// area's reservation list after all the fields are set.
		setArea(area);
	}




	/* ---- Setters ---- */

	/**
	 * Sets the area of the reservation.
	 * Also checks if the area is free for the specified timespan.
	 * Also adds the reservation to the area's reservation list.
	 *
	 * @param area to reserve
	 * @throws IllegalArgumentException if area is null
	 * @throws IllegalStateException if area is not free for the specified timespan
	 */
	private void setArea(Area area) throws IllegalArgumentException, IllegalStateException {
		if (area == null) {
			throw new IllegalArgumentException("Area cannot be null");
		}
		if (!area.isFreeBetween(startDateTime, endDateTime)) {
			throw new IllegalStateException(
				"Cannot create reservation, area is not free for the specified timespan"
			);
		}
		area.addReservation(this);
		this.area = area;
	}

	/**
	 * Sets the user of the reservation.
	 * Also adds the reservation to the user's reservation list.
	 *
	 * @param user that is reserving the area
	 * @throws IllegalArgumentException if user is null
	 */
	private void setUser(User user) throws IllegalArgumentException {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		this.user = user;
		user.addReservation(this);
	}

	/**
	 * Sets the start date and time of the reservation.
	 *
	 * @param start date and time of reservation start
	 * @throws IllegalArgumentException if start is null
	 * @throws IllegalStateException if start is before the current time
	 */
	private void setStart(LocalDateTime start)
		throws IllegalArgumentException, IllegalStateException {
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
	 * @throws IllegalArgumentException if end is null
	 * @throws IllegalStateException if end is before the start time
	 */
	private void setEnd(LocalDateTime end) throws IllegalArgumentException, IllegalStateException {
		if (end == null) {
			throw new IllegalArgumentException("End time cannot be null");
		}
		if (end.isBefore(startDateTime)) {
			throw new IllegalStateException("End time cannot be before start time");
		}
		this.endDateTime = end;
	}

	/**
	 * Sets the comment for the reservation.
	 *
	 * @param comment for the reservation
	 * @throws IllegalArgumentException if comment is null or blank
	 */
	private void setComment(String comment) throws IllegalArgumentException {
		if (comment == null || comment.isBlank()) {
			throw new IllegalArgumentException("Comment cannot be blank or null");
		}
		this.comment = comment;
	}

	/* ---- Getters ---- */

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
