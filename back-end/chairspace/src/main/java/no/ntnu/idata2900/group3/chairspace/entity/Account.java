package no.ntnu.idata2900.group3.chairspace.entity;

import java.util.Set;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import no.ntnu.idata2900.group3.chairspace.dto.AccountDto;

/**
 * Represents a user.
 */
@Entity
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID user_id;
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
	 * Account constructor using a DTO.
	 *
	 * @param accountDto Account DTO
	 */
	public Account(AccountDto accountDto) {

	}

	/**
	 * Account constructor with arguments.
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
	 * @return Id as string
	 */
	public UUID getId() {
		return this.user_id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public int getPhoneNumber() {
		return this.phoneNumber;
	}

	/* ---- Setters ---- */

	public void setId(UUID id) {
		this.user_id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setAdministrates(Set<Area> administrates) {
		this.administrates = administrates;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	/* ---- Reservations ---- */

	public void addReservation(Reservation reservation) {
		if (this.reservations == null) {
			throw new IllegalArgumentException("Reservations is null");
		}
		if (!this.reservations.contains(reservation)) {
			this.reservations.add(reservation);
		}
	}

	public void removeReservation(Reservation reservation) {
		if(this.reservations.isEmpty()) {
			throw new IllegalStateException("No reservations to remove");
		}
		this.reservations.remove(reservation);
	}

	/* ---- Area permissions ---- */

	public void addArea(Area area) {
		if (this.administrates == null) {
			throw new IllegalArgumentException("Administrates is null");
		}
		if (!this.administrates.contains(area)) {
			this.administrates.add(area);
		}
	}

	public void removeArea(Area area) {
		if(this.administrates.isEmpty()) {
			throw new IllegalStateException("No areas to remove");
		}
		if (!this.administrates.contains(area)) {
			throw new IllegalArgumentException("Area not found");
		}
		this.administrates.remove(area);
	}
}
