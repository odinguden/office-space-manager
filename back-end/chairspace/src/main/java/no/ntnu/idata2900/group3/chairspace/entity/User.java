package no.ntnu.idata2900.group3.chairspace.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;

/**
 * Represents a user.
 * Contains information about the user, such as name,
 * email,
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
public class User implements EntityInterface<UUID> {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@Column(name = "first_name", nullable = false)
	private String firstName;
	@Column(name = "last_name", nullable = false)
	private String lastName;
	@Column(nullable = false)
	private String email;
	@Column(name = "is_admin", nullable = false)
	private boolean isAdmin = false;
	@Column(name = "external_id", nullable = true)
	private String externalId = null;

	/**
	 * No args constructor for JPA.
	 */
	public User() {}

	/**
	 * Constructs a instance of user with first and last name and a email.
	 *
	 * @param firstName the first name of the user
	 * @param lastName the last name of the user
	 * @param email the email of the user
	 * @throws InvalidArgumentCheckedException if any of the parameters are empty
	 */
	public User(String firstName, String lastName, String email, String exsternalID)
		throws InvalidArgumentCheckedException {
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		//TODO: proper setter for externalId
		this.externalId = exsternalID;

	}

	/* ---- Getters ---- */

	/**
	 * Returns the id of the user as a string.
	 *
	 * @return The id of the user
	 */
	public UUID getId() {
		return this.id;
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

	/* ---- Setters ---- */

	/**
	 * Sets email for user.
	 *
	 * @param email email string.
	 * @throws InvalidArgumentCheckedException if email string is blank
	 * @throws InvalidArgumentCheckedException if email string does not mach email format
	 */
	public void setEmail(String email) throws InvalidArgumentCheckedException {
		if (email == null) {
			throw new IllegalArgumentException("Email is null when value was expected");
		}
		if (email.isBlank()) {
			throw new InvalidArgumentCheckedException("Email is blank");
		}
		// Regex written by Sigve Bjørkedal
		String patternString = "^[^@\\s]+@[^@\\s]+\\..{1,4}$";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new InvalidArgumentCheckedException("Email is not valid");
		}
		this.email = email;
	}

	/**
	 * Sets the first name of the user.
	 *
	 * @param firstName the first name of the user
	 * @throws InvalidArgumentCheckedException if first name is null
	 */
	public void setFirstName(String firstName)  throws InvalidArgumentCheckedException {
		if (firstName == null) {
			throw new IllegalArgumentException("firstName is null when value was expected");
		}
		if (firstName.isBlank()) {
			throw new InvalidArgumentCheckedException("firstName is blank");
		}
		this.firstName = firstName;
	}

	/**
	 * Sets the last name string of the user.
	 *
	 * @param lastName the last name of the user
	 * @throws InvalidArgumentCheckedException if the last name string is blank
	 */
	public void setLastName(String lastName) throws InvalidArgumentCheckedException {
		if (lastName == null) {
			throw new IllegalArgumentException("lastName is null when value was expected");
		}
		if (lastName.isBlank()) {
			throw new InvalidArgumentCheckedException("lastName is blank");
		}
		this.lastName = lastName;
	}
}
