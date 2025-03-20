package no.ntnu.idata2900.group3.chairspace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;

/**
 * Represents a type that an area can be such as "office" or "meeting room".
 *
 * @author Odin Lyngsgård
 * @version 1.0
 * @since 0.1
 * @see Area
 */
@Entity
public class AreaType {
	@Id
	private String name;
	private String description;

	/**
	 * No args constructor for JPA.
	 */
	public AreaType() {}

	/**
	 * Constructor for AreaType.
	 *
	 * @param name of the area type
	 * @param description of the area type
	 * @throws InvalidArgumentCheckedException if name or description is null or blank
	 */
	public AreaType(String name, String description) throws InvalidArgumentCheckedException {
		setName(name);
		setDescription(description);
	}

	/**
	 * Constructor for AreaType.
	 *
	 * @param name of the area type
	 * @throws InvalidArgumentCheckedException if name or description is null or blank
	 */
	public AreaType(String name) throws InvalidArgumentCheckedException {
		setName(name);
		setDescription("");
	}

	/* ---- Getters ---- */

	/**
	 * Gets the name of the area type as a string.
	 *
	 * @return the name of the area type as string
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description of the area type as a string.
	 *
	 * @return the description of the area type
	 */
	public String getDescription() {
		return description;
	}

	/* ---- Setters ---- */

	/**
	 * Gets the name of the area type as string.
	 *
	 * @param name of the area type
	 * @throws InvalidArgumentCheckedException if name is  empty
	 */
	private void setName(String name) throws InvalidArgumentCheckedException {
		if (name == null) {
			throw new InvalidArgumentCheckedException("Name is null when a value was expected");
		}
		if (name.isEmpty()) {
			throw new InvalidArgumentCheckedException("Name cannot be empty");
		}
		this.name = name;
	}

	/**
	 * Updates the description .
	 *
	 * @param description the new description of the area type.
	 */
	public void setDescription(String description) {
		if (description == null) {
			throw new IllegalArgumentException("Description is null when value was expected");
		}
		setDescription(description);
	}
}
