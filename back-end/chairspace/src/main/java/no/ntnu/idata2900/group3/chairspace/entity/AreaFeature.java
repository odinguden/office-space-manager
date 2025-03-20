package no.ntnu.idata2900.group3.chairspace.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;

/**
 * An area feature is a feature that an area can have, such as "wheelchair accessible" or "outdoor".
 * The area feature object contains the name and description of the feature.
 * And can relate to multiple areas. An area can also have multiple features.
 *
 * <p>
 * Is used by the {@link Area} class to represent the features of an area.
 *
 * @author Odin Lyngsgård
 * @version 1.0
 * @since 0.1
 * @see Area
 */
@Schema(description = "Represents an area feature in the database")
@Entity
public class AreaFeature {
	@Id
	private String name;
	private String description;

	/**
	 * No args constructor for JPA.
	 */
	public AreaFeature() {}

	/**
	 * Constructor for AreaFeature.
	 * Initializes the name and description of the area feature.
	 *
	 * <p>
	 * also initializes an empty area set to save the areas that have this feature.
	 *
	 * @param name Name of the area feature
	 * @param description Description of the area feature
	 * @throws InvalidArgumentCheckedException if name is blank
	 */
	public AreaFeature(String name, String description) throws InvalidArgumentCheckedException {
		setName(name);
		setDescription(description);
	}

	/**
	 * Constructor for AreaFeature.
	 * Initializes the name and description of the area feature.
	 * throws IllegalArgumentException if name is null
	 *
	 * <p>
	 * also initializes an empty area set to save the areas that have this feature.
	 *
	 * @param name Name of the area feature
	 * @throws InvalidArgumentCheckedException if name is  blank
	 */
	public AreaFeature(String name) throws InvalidArgumentCheckedException {
		setName(name);
		setDescription("");
	}


	/* ---- Getters ---- */

	/**
	 * Returns the name of the area feature.
	 *
	 * @return The name of the area feature
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description of the area feature.
	 *
	 * @return The description of the area feature
	 */
	public String getDescription() {
		return description;
	}



	/* ---- Setters ---- */

	/**
	 * Sets the name of the area feature.
	 *
	 * @param name Name as String
	 * @throws InvalidArgumentCheckedException if the name is blank
	 */
	private void setName(String name) throws InvalidArgumentCheckedException {
		if (name == null) {
			throw new IllegalArgumentException("Name is null when value was expected.");
		}
		if (name.isBlank()) {
			throw new InvalidArgumentCheckedException("Name cannot be blank");
		}
		this.name = name;
	}

	/**
	 * Updates the description of the area feature.
	 *
	 * @param description Description as String
	 */
	public void setDescription(String description) {
		if (description == null) {
			throw new IllegalArgumentException("Description is null when value was expected");
		}
		this.description = description;
	}
}
