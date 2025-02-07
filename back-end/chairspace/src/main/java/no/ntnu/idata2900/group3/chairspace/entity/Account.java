package no.ntnu.idata2900.group3.chairspace.entity;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

/**
 * Represents a user.
 */
@Entity
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int user_id;
	private String firstName;
	private String lastName;
	private String email;
	private int phoneNumber;
	@ManyToMany
	private Set<Area> administrates;
	@OneToMany
	private Set<Reservation> reservations;

	public Account() {}

	/* ---- Getters ---- */

	public int getId() {
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

	public void setId(int id) {
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
	

}
