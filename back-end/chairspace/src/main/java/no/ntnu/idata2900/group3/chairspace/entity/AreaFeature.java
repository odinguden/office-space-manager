package no.ntnu.idata2900.group3.chairspace.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an area feature in the database.
 * Contains the name and description of the area feature.
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
	@ManyToMany(mappedBy = "features")
	private Set<Area> areas;

	/**
	 * No args constructor for JPA.
	 */
	public AreaFeature() {}

	/**
	 * Constructor for AreaFeature.
	 * Initializes the name and description of the area feature.
	 * throws IllegalArgumentException if name is null or blank.
	 *
	 * <p>
	 * also initializes an empty area set to save the areas that have this feature.
	 *
	 * @param name Name of the area feature
	 * @param description Description of the area feature
	 * @throws InvalidArgumentCheckedException if name or description is null or blank
	 */
	public AreaFeature(String name, String description) throws InvalidArgumentCheckedException {
		setName(name);
		setDescription(description);
		areas = new HashSet<>();
	}

	/**
	 * Constructor for AreaFeature.
	 * Initializes the name and description of the area feature.
	 * throws IllegalArgumentException if name or description is null or blank.
	 *
	 * <p>
	 * also initializes an empty area set to save the areas that have this feature.
	 *
	 * @param name Name of the area feature
	 * @param description Description of the area feature
	 * @throws InvalidArgumentCheckedException if name or description is null or blank
	 */
	public AreaFeature(String name) throws InvalidArgumentCheckedException {
		setName(name);
		setDescription("");
		areas = new HashSet<>();
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

	/**
	 * Returns the areas that have this feature.
	 *
	 * @return The areas that have this feature in a set
	 */
	public Set<Area> getAreas() {
		return areas;
	}

	/* ---- Setters ---- */

	/**
	 * Sets the name of the area feature.
	 *
	 * @param name Name as String
	 * @throws InvalidArgumentCheckedException if the name is null or blank
	 */
	private void setName(String name) throws InvalidArgumentCheckedException {
		if (name == null || name.isBlank()) {
			throw new InvalidArgumentCheckedException("Name cannot be null or blank.");
		}
		this.name = name;
	}

	private void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Updates the description of the area feature.
	 *
	 * @param description Description as String
	 * @throws InvalidArgumentCheckedException if the description is null or blank
	 */
	public void updateDescription(String description) throws InvalidArgumentCheckedException {
		if (description == null || description.isBlank()) {
			throw new InvalidArgumentCheckedException("Updated description cannot be null or blank.");
		}
		setDescription(description);
	}

	/**
	 * Sets the areas that have this feature.
	 *
	 * @param areas Areas as Set
	 * @throws InvalidArgumentCheckedException if areas is null
	 */
	public void addAreas(Set<Area> areas) throws InvalidArgumentCheckedException {
		if (areas == null) {
			throw new InvalidArgumentCheckedException("Areas cannot be null.");
		}
		for (Area area : areas) {
			addArea(area);
		}
	}

	/**
	 * Adds an area to the list of areas that have this feature.
	 *
	 * @param area Area to add
	 * @throws InvalidArgumentCheckedException if area is null
	 */
	public void addArea(Area area) throws InvalidArgumentCheckedException {
		if (area == null) {
			throw new InvalidArgumentCheckedException("Area cannot be null.");
		}
		areas.add(area);
	}

	/**
	 * Removes given area from areas set.
	 *
	 * @param area area object to remove
	 * @throws InvalidArgumentCheckedException area to remove is null
	 */
	public void removeArea(Area area) throws InvalidArgumentCheckedException {
		if (area == null) {
			throw new InvalidArgumentCheckedException("Area cannot be null");
		}
		areas.remove(area);
	}
}
