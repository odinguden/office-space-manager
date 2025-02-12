package no.ntnu.idata2900.group3.chairspace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a user.
 */
@Entity
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID userUuid;
	private String firstName;
	private String lastName;
	private String email;
	private int phoneNumber;
	@ManyToMany
	private Set<Area> administrates;
	@OneToMany
	private Set<Reservation> reservations;

	/**
	 * No args constructor for JPA.
	 */
	public Account() {}

	/**
	 * Account constructor with arguments.
	 *
	 * @param firstName First name as string
	 * @param lastName Last name as string
	 * @param email Email as string
	 * @param phoneNumber Phone number as int
	 */
	public Account(String firstName, String lastName, String email, int phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
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
	public void setId(UUID id) {
		this.userUuid = id;
	}

	/**
	 * Sets the first name of the account.
	 *
	 * @param firstName First name as string
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Sets the last name of the account.
	 *
	 * @param lastName Last name as string
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Sets the email of the account.
	 *
	 * @param email Email as string
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets the phone number of the account.
	 *
	 * @param phoneNumber Phone number as int
	 */
	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Sets the areas that the account administrates.
	 *
	 * @param administrates Set of areas
	 */
	public void setAdministrates(Set<Area> administrates) {
		this.administrates = administrates;
	}

	/**
	 * Sets the reservations that the account has.
	 *
	 * @param reservations Set of reservations
	 */
	public void setReservations(Set<Reservation> reservations) {
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
}
