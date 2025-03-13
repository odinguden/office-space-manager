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
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
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
	 * @return Id as string
	 */
	public UUID getId() {
		return this.userUuid;
	}

	/**
	 * returns the users reservations in a set.
	 *
	 * @return reservations in a set
	 */
	public Set<Reservation> getReservations() {
		return reservations;
	}

	/**
	 * Returns the first name of the account as a string.
	 *
	 * @return First name as string
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Returns last name of the account as a string.
	 *
	 * @return Last name as string
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Returns the email of the account as a string.
	 *
	 * @return Email as string
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Returns the phone number of the account as an int.
	 *
	 * @return Phone number as int
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	/**
	 * Returns areas that the user administrates in a set.
	 *
	 * @return areas in a set
	 */
	public Set<Area> getAreas() {
		return administrates;
	}

	/* ---- Setters ---- */


	/* ---- Reservations ---- */

	/**
	 * Adds multiple reservations.
	 *
	 * @param reservations reservations in a set
	 */
	public void addReservations(Set<Reservation> reservations) {
		if (reservations == null) {
			throw new IllegalArgumentException("Reservations is null");
		}
		for (Reservation reservation : reservations) {
			addReservation(reservation);
		}
	}

	/**
	 * Adds a single reservation to the account.
	 *
	 * @param reservation Reservation object
	 */
	public void addReservation(Reservation reservation) {
		if (reservation == null) {
			throw new IllegalArgumentException();
		}
		this.reservations.add(reservation);
	}

	/**
	 * Removes a single reservation from the account.
	 *
	 * @param reservation Reservation object
	 */
	public void removeReservation(Reservation reservation) {
		this.reservations.remove(reservation);
	}

	/* ---- Area permissions ---- */

	/**
	 * Adds an area to the account.
	 *
	 * @param area Area object
	 */
	public void addArea(Area area) {
		if (area == null) {
			throw new IllegalArgumentException("Area is null");
		}
		this.administrates.add(area);
	}

	/**
	 * Removes an area from the account.
	 *
	 * @param area Area object
	 */
	public void removeArea(Area area) {
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
		public Builder(String firstName, String lastName) {
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
		 */
		public Builder email(String email) {
			String patternString = "^[^@\\s]+@[^@\\s]+\\..{1,4}$";
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(email);
			if (email == null || !matcher.matches()) {
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
		public Builder area(Area area) {
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
		public Builder areas(Set<Area> areas) {
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
