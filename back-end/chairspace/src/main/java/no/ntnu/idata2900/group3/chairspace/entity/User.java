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

/**
 * Represents a user.
 *
 * @TODO Implement factory pattern for creating users.
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
	private int phoneNumber;
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

	/**
	 * Account constructor with arguments.
	 *
	 * @param firstName First name as string
	 * @param lastName Last name as string
	 * @param email Email as string
	 * @param phoneNumber Phone number as int
	 */
	public User(String firstName, String lastName, String email, int phoneNumber) {
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setPhoneNumber(phoneNumber);
		setAdministrates(
			new HashSet<>()
		);
		setReservations(
			new HashSet<>()
		);
	}

	private User(Builder builder) {
		setFirstName(builder.firstName);
		setLastName(builder.lastName);
		setEmail(builder.email);
		setPhoneNumber(builder.phoneNumber);

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
	 * TODO: Should this be a string? or some other type?
	 *
	 * @return Phone number as int
	 */
	public int getPhoneNumber() {
		return this.phoneNumber;
	}

	/* ---- Setters ---- */

	/**
	 * Sets the id of the user.
	 *
	 * @param id UUID
	 */
	private void setId(UUID id) {
		if (id == null) {
			throw new IllegalArgumentException("Id is null");
		}
		this.userUuid = id;
	}

	/**
	 * Sets the first name of the account.
	 *
	 * @param firstName First name as string
	 */
	private void setFirstName(String firstName) {
		if (firstName == null || firstName.isEmpty()) {
			throw new IllegalArgumentException("First name is null");
		}
		this.firstName = firstName;
	}

	/**
	 * Sets the last name of the account.
	 *
	 * @param lastName Last name as string
	 */
	private void setLastName(String lastName) {
		if (lastName == null || lastName.isEmpty()) {
			throw new IllegalArgumentException("Last name is null");
		}
		this.lastName = lastName;
	}

	/**
	 * Sets the email of the account.
	 *
	 * @param email Email as string
	 */
	private void setEmail(String email) {
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("Email is null");
		}
		this.email = email;
	}

	/**
	 * Sets the phone number of the account.
	 *
	 * @param phoneNumber Phone number as int
	 */
	private void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Sets the areas that the account administrates.
	 *
	 * @param administrates Set of areas
	 */
	private void setAdministrates(Set<Area> administrates) {
		this.administrates = administrates;
	}

	/**
	 * Sets the reservations that the account has.
	 *
	 * @param reservations Set of reservations
	 */
	private void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	/* ---- Reservations ---- */

	/**
	 * Adds a single reservation to the account.
	 *
	 * @param reservation Reservation object
	 */
	public void addReservation(Reservation reservation) {
		if (this.reservations == null) {
			throw new IllegalArgumentException("Reservations is null");
		}
		if (!this.reservations.contains(reservation)) {
			this.reservations.add(reservation);
		}
	}

	/**
	 * Removes a single reservation from the account.
	 *
	 * @param reservation Reservation object
	 */
	public void removeReservation(Reservation reservation) {
		if (this.reservations.isEmpty()) {
			throw new IllegalStateException("No reservations to remove");
		}
		this.reservations.remove(reservation);
	}

	/* ---- Area permissions ---- */

	/**
	 * Adds an area to the account.
	 *
	 * @param area Area object
	 */
	public void addArea(Area area) {
		if (this.administrates == null) {
			throw new IllegalArgumentException("Administrates is null");
		}
		if (!this.administrates.contains(area)) {
			this.administrates.add(area);
		}
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
		private int phoneNumber;
		private Set<Area> administrates;
		private Set<Reservation> reservations;
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
			this.reservations = new HashSet<>();
		}

		/**
		 * Sets the email of the user.
		 *
		 * @param email Email as string
		 * @return UserBuilder object
		 */
		public Builder email(String email) {
			if (email == null || email.isEmpty()) {
				throw new IllegalArgumentException("Email is null");
			}
			this.email = email;
			return this;
		}

		/**
		 * Sets the phone number of the user.
		 *
		 * @param phoneNumber Phone number as int
		 * @return UserBuilder object
		 * @throws IllegalArgumentException if phone number is null
		 */
		public Builder phoneNumber(int phoneNumber) {
			if (phoneNumber == 0) {
				throw new IllegalArgumentException("Phone number is null");
			}
			this.phoneNumber = phoneNumber;
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
				this.administrates.add(area);
			}

			return this;
		}

		/**
		 * Adds a single reservation to the user.
		 *
		 * @param reservation Reservation object
		 * @return UserBuilder object
		 * @throws IllegalArgumentException if reservation is null
		 */
		public Builder reservation(Reservation reservation) {
			if (reservation == null) {
				throw new IllegalArgumentException("Reservation is null");
			}
			this.reservations.add(reservation);

			return this;
		}

		/**
		 * Adds multiple reservations to the user.
		 * This should only be used for testing purposes.
		 * If you are creating a new user, then reservations should not be added upon creation.
		 *
		 * @param reservations Set of reservations
		 * @return UserBuilder object
		 * @throws IllegalArgumentException if reservations is null
		 */
		public Builder reservations(Set<Reservation> reservations) {
			if (reservations == null) {
				throw new IllegalArgumentException("Reservations is null");
			}
			for (Reservation reservation : reservations) {
				this.reservations.add(reservation);
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
