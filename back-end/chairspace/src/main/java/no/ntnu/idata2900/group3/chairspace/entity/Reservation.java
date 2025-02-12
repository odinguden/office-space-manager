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
 * @TODO Consider using factory pattern for creating reservations.
 */
@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@ManyToOne
	private Area area;
	@ManyToOne
	private Account user;
	private LocalDateTime start;
	private LocalDateTime end;
	private String comment;

	/**
	 * No args constructor for JPA.
	 */
	public Reservation() {
		// No args constructor for JPA.
	}

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
		Account account,
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
	private void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Sets the area of the reservation.
	 *
	 * @param area to reserve
	 */
	private void setArea(Area area) {
		this.area = area;
	}

	/**
	 * Sets the user of the reservation.
	 *
	 * @param user that is reserving the area
	 */
	private void setUser(Account user) {
		this.user = user;
	}

	/**
	 * Sets the start date and time of the reservation.
	 *
	 * @param start date and time of reservation
	 */
	private void setStart(LocalDateTime start) {
		this.start = start;
	}

	/**
	 * Sets the end date and time of the reservation.
	 *
	 * @param end date and time of reservation
	 */
	private void setEnd(LocalDateTime end) {
		this.end = end;
	}

	/**
	 * Sets the comment for the reservation.
	 *
	 * @param comment for the reservation
	 */
	private void setComment(String comment) {
		this.comment = comment;
	}

	/* ---- Getters ---- */

	/**
	 * Returns id as UUID.
	 *
	 * @return Id as UUID
	 */
	public UUID getId() {
		return id;
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
	public Account getUser() {
		return user;
	}

	/**
	 * Returns the start date and time of the reservation.
	 *
	 * @return Start date and time of reservation
	 */
	public LocalDateTime getStart() {
		return start;
	}

	/**
	 * Returns the end date and time of the reservation.
	 *
	 * @return End date and time of reservation
	 */
	public LocalDateTime getEnd() {
		return end;
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
