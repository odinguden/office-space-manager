package no.ntnu.idata2900.group3.chairspace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.ReservedException;

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
	 * @throws ReservedException if the area is not free for the specified timespan
	 * @throws InvalidArgumentCheckedException if the end time is before the start time
	 * @throws InvalidArgumentCheckedException if the start time is before the current time
	 */
	public Reservation(
		Area area,
		User user,
		LocalDateTime start,
		LocalDateTime end,
		String comment
	) throws InvalidArgumentCheckedException, ReservedException {
		setUser(user);
		setTimes(start, end);
		setComment(comment);
		// Area needs to be set last. This is to ensure that the reservation is added to the
		// area's reservation list after all the fields are set.
		setArea(area);
	}

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
	 * @throws ReservedException if the area is not free for the specified timespan
	 * @throws InvalidArgumentCheckedException if the end time is before the start time
	 * @throws InvalidArgumentCheckedException if the start time is before the current time
	 */
	public Reservation(
		Area area,
		User user,
		LocalDateTime start,
		LocalDateTime end
	) throws InvalidArgumentCheckedException, ReservedException {
		setUser(user);
		setTimes(start, end);
		setComment("");
		setArea(area);
	}



	/* ---- Setters ---- */

	/**
	 * Sets the area of the reservation.
	 * Also checks if the area is free for the specified timespan.
	 * Also adds the reservation to the area's reservation list.
	 *
	 * @param area to reserve
	 * @throws ReservedException if area is not free for the specified timespan
	 */
	private void setArea(Area area) throws ReservedException {
		if (area == null) {
			throw new IllegalArgumentException("Area was null when value was expected");
		}
		if (!area.isFreeBetween(startDateTime, endDateTime)) {
			throw new ReservedException(
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
	 */
	private void setUser(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User was null when value was expected");
		}
		this.user = user;
	}

	/**
	 * Sets start and end time for the reservation.
	 * Start time must be after current time and before end time.
	 *
	 * @param start start time of the reservation
	 * @param end end time of the reservation
	 * @throws InvalidArgumentCheckedException if the start time is after the end time
	 * @throws InvalidArgumentCheckedException if the start time is before current time

	 */
	private void setTimes(LocalDateTime start, LocalDateTime end)
		throws InvalidArgumentCheckedException {
		if (start == null) {
			throw new IllegalArgumentException("start is null where value was expected");
		}
		if (end == null) {
			throw new IllegalArgumentException("End is null where value was expected");
		}
		if (end.isBefore(start)) {
			throw new InvalidArgumentCheckedException("Start time is before end time");
		}
		if (start.isBefore(LocalDateTime.now())) {
			throw new InvalidArgumentCheckedException(
				"Start time for the reservation is before current time"
			);
		}
		this.startDateTime = start;
		this.endDateTime = end;
	}

	/**
	 * Sets the comment for the reservation.
	 *
	 * @param comment for the reservation
	 */
	private void setComment(String comment) {
		if (comment == null) {
			throw new IllegalArgumentException("Comment is null when a value was expected");
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

	/* ---- Methods ---- */

	/**
	 * Checks if a timespan collides with the reservation.
	 * A timespan consists of a start time and a end time.
	 * It collides with the reservation if the timespan starts or ends within the timespan or if it
	 * encompasses it.
	 *
	 * @param start start of timespan
	 * @param end end of timespan
	 * @return True if reservations collides with the provided timespan.
	 */
	public boolean doesCollide(LocalDateTime start, LocalDateTime end) {
		if (start == null) {
			throw new IllegalArgumentException("start was null when value was expected");
		}
		if (end == null) {
			throw new IllegalArgumentException("end was null when value was expected");
		}
		boolean doesCollide = false;
		if (doesCollide(start) || doesCollide(end)) {
			//Timespan ends or starts during reservation
			doesCollide = true;
		}
		if (start.isBefore(startDateTime) && end.isAfter(endDateTime)) {
			//Timespan encompasses reservation
			doesCollide = true;
		}
		return doesCollide;
	}

	/**
	 * Returns true if this time point collides with the reservation.
	 * Collides means that it falls between the start and end time of the reservation.
	 *
	 * @param timePoint time to check
	 * @return true if the timePoint falls between the start and end of the reservation.
	 */
	public boolean doesCollide(LocalDateTime timePoint) {
		if (timePoint == null) {
			throw new IllegalArgumentException("timePoint is null when a value was expected");
		}
		boolean doesCollide = false;
		if (timePoint.isAfter(startDateTime) && timePoint.isBefore(endDateTime)) {
			doesCollide = true;
		}
		return doesCollide;
	}
}
