package no.ntnu.idata2900.group3.chairspace.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a user.
 * Contains information about the user, such as name,
 * email,areas they administrate and reservations.
 *
 * <p>
 * implements the builder pattern.
 *
 * @author Odin Lyngsgård
 * @version 0.1
 * @since 0.1
 */
@Entity
@Schema(description = "Represents a user in the database")
@Table(name = "accounts")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID userUuid;
	@Column(name = "first_name", nullable = false)
	private String firstName;
	@Column(name = "last_name", nullable = false)
	private String lastName;
	@Column(nullable = false)
	private String email;
	@ManyToMany(mappedBy = "administrators")
	@Column(nullable = true)
	private Set<Area> administrates;
	@OneToMany
	@Column(nullable = true)
	@JoinTable(
		name = "account_reservations",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "reservation_id")
	)
	private Set<Reservation> reservations;

	/**
	 * No args constructor for JPA.
	 */
	public User() {}

	private User(Builder builder) {
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.email = builder.email;
		this.administrates = builder.administrates;
		reservations = new HashSet<>();
	}

	/* ---- Getters ---- */

	/**
	 * Returns the id of the user as a string.
	 *
	 * @return The id of the user
	 */
	public UUID getId() {
		return this.userUuid;
	}

	/**
	 * returns the users reservations in a set.
	 *
	 * @return reservations made by the user
	 */
	public Set<Reservation> getReservations() {
		return reservations;
	}

	/**
	 * Returns the first name of the account as a string.
	 *
	 * @return The first name of the user
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Returns last name of the account as a string.
	 *
	 * @return The last name of the user
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Returns the email of the account as a string.
	 *
	 * @return The email of the user
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Returns areas that the user administrates in a set.
	 *
	 * @return Areas administrated by the user
	 */
	public Set<Area> getAreas() {
		return administrates;
	}

	/* ---- Reservations ---- */

	/**
	 * Adds multiple reservations to the user.
	 *
	 * @param reservations reservations in a set
	 * @throws IllegalArgumentException if reservations is null
	 */
	public void addReservations(Set<Reservation> reservations) throws IllegalArgumentException {
		if (reservations == null) {
			throw new IllegalArgumentException("Reservations is null");
		}
		for (Reservation reservation : reservations) {
			addReservation(reservation);
		}
	}

	/**
	 * Adds a single reservation to the user.
	 *
	 * @param reservation Reservation object
	 * @throws IllegalArgumentException if reservation is null
	 */
	public void addReservation(Reservation reservation) throws IllegalArgumentException {
		if (reservation == null) {
			throw new IllegalArgumentException();
		}
		this.reservations.add(reservation);
	}

	/**
	 * Removes a single reservation from the account.
	 *
	 * @param reservation Reservation object
	 * @throws IllegalArgumentException if reservation is null
	 */
	public void removeReservation(Reservation reservation) throws IllegalArgumentException {
		this.reservations.remove(reservation);
	}

	/* ---- Area permissions ---- */

	/**
	 * Adds an area to administrate.
	 *
	 * @param area Area object
	 * @throws IllegalArgumentException if area is null
	 */
	public void addArea(Area area) throws IllegalArgumentException {
		if (area == null) {
			throw new IllegalArgumentException("Area is null");
		}
		this.administrates.add(area);
	}

	/**
	 * Removes an area from the account.
	 *
	 * @param area Area object
	 * @throws IllegalStateException if there are no areas to remove
	 * @throws IllegalArgumentException if area is not found
	 */
	public void removeArea(Area area) throws IllegalStateException, IllegalArgumentException {
		if (this.administrates.isEmpty()) {
			throw new IllegalStateException("No areas to remove");
		}
		if (!this.administrates.contains(area)) {
			throw new IllegalArgumentException("Area not found");
		}
		this.administrates.remove(area);
	}

	/**
	 * Builder class for User.
	 * Implements the builder pattern.
	 *
	 * <p>
	 * first name and last name are required fields.
	 * email is optional.
	 * areas are optional.
	 */
	public static class Builder {

		private String firstName;
		private String lastName;
		private String email;
		private Set<Area> administrates;
		// Im sure you're wondering why there are private variables, and no getters.
		// If a class that is not the userBuilder or the user needs the values, then they are using
		// the builder pattern wrong.

		/**
		 * Constructor for UserBuilder.
		 *
		 * @param firstName First name as string
		 * @param lastName Last name as string
		 * @throws IllegalArgumentException if first name or last name is null
		 */
		public Builder(String firstName, String lastName) throws IllegalArgumentException {
			if (firstName == null || firstName.isEmpty()) {
				throw new IllegalArgumentException("First name is null");
			}
			if (lastName == null || lastName.isEmpty()) {
				throw new IllegalArgumentException("Last name is null");
			}
			this.lastName = lastName;
			this.firstName = firstName;
			this.administrates = new HashSet<>();
		}

		/**
		 * Sets the email of the user.
		 *
		 * @param email Email as string
		 * @return UserBuilder object
		 * @throws IllegalArgumentException if email is null or blank
		 * @throws IllegalArgumentException if email is not valid
		 */
		public Builder email(String email) throws IllegalArgumentException {
			if (email == null || email.isBlank()) {
				throw new IllegalArgumentException("Email is null or blank");
			}
			// Regex written by Sigve Bjørkedal
			String patternString = "^[^@\\s]+@[^@\\s]+\\..{1,4}$";
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(email);
			if (!matcher.matches()) {
				throw new IllegalArgumentException("Email is not valid");
			}
			this.email = email;
			return this;
		}

		/**
		 * Adds an area to the user.
		 *
		 * @param area Area object
		 * @return UserBuilder object
		 * @throws IllegalArgumentException if area is null
		 */
		public Builder area(Area area) throws IllegalArgumentException {
			if (area == null) {
				throw new IllegalArgumentException("Area is null");
			}
			this.administrates.add(area);

			return this;
		}

		/**
		 * Adds multiple areas to the user.
		 *
		 * @param areas Set of areas
		 * @return UserBuilder object
		 * @throws IllegalArgumentException if areas is null
		 */
		public Builder areas(Set<Area> areas) throws IllegalArgumentException {
			if (areas == null) {
				throw new IllegalArgumentException("Areas is null");
			}
			for (Area area : areas) {
				area(area);
			}

			return this;
		}

		/**
		 * Builds the User object.
		 *
		 * @return User object
		 */
		public User build() {
			return new User(this);
		}

	}
}
