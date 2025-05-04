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
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;
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
	@Column(name = "name", nullable = false)
	private String name;
	@Column(nullable = false)
	private String email;
	@Column(name = "is_admin", nullable = false)
	private boolean isAdmin = false;
	@Column(name = "external_id", nullable = true)
	private String externalId = null;
	@ManyToMany
	@JoinTable(
		name = "user_favorites",
		joinColumns = @JoinColumn(name = "area_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	Set<Area> favoriteAreas;

	/**
	 * No args constructor for JPA.
	 */
	public User() {}

	/**
	 * Constructs a instance of user with first and last name and a email.
	 *
	 * @param name the name of the user
	 * @param email the email of the user
	 * @param externalId the external id of the user
	 * @throws InvalidArgumentCheckedException if any of the parameters are empty
	 */
	public User(String name, String email, String externalId)
		throws InvalidArgumentCheckedException {
		setName(name);
		setEmail(email);
		favoriteAreas = new HashSet<>();
		//TODO: proper setter for externalId
		this.externalId = externalId;

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
	 * Returns the name of the user as a string.
	 *
	 * @return The name of the user
	 */
	public String getName() {
		return this.name;
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
	 * Returns the admin status of the user.
	 *
	 * @return true if the user is an admin
	 */
	public boolean isAdmin() {
		return this.isAdmin;
	}

	/**
	 * Return the users favorite areas as a set.
	 *
	 * @return the set of favorite areas
	 */
	public Set<Area> getFavoriteAreas() {
		return favoriteAreas;
	}

	/* ---- Setters ---- */

	/**
	 * Sets the admin status of the user.
	 *
	 * @param isAdmin true if the user is an admin, false otherwise
	 */
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

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
	 * Sets the name of the user.
	 *
	 * @param name the name of the user
	 * @throws InvalidArgumentCheckedException if name is null or blank
	 */
	public void setName(String name)  throws InvalidArgumentCheckedException {
		if (name == null) {
			throw new IllegalArgumentException("name is null when value was expected");
		}
		if (name.isBlank()) {
			throw new InvalidArgumentCheckedException("firstName is blank");
		}
		this.name = name;
	}

	/**
	 * Adds an area to the list of favorite areas.
	 *
	 * @param area the area to add
	 */
	public void addFavoriteArea(Area area) {
		if (area == null) {
			throw new IllegalArgumentException("area is null when value was expected");
		}
		favoriteAreas.add(area);
	}

	/**
	 * Removes an area from the list of favorite areas.
	 *
	 * @param area the area to remove
	 */
	public void removeFavoriteArea(Area area) {
		if (area == null) {
			throw new IllegalArgumentException("area is null when value was expected");
		}
		if (favoriteAreas != null) {
			favoriteAreas.remove(area);
		}
	}
}
