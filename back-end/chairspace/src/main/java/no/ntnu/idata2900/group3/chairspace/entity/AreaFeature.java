package no.ntnu.idata2900.group3.chairspace.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
	 * throws IllegalArgumentException if name or description is null or blank.
	 *
	 * <p>
	 * also initializes an empty area set to save the areas that have this feature.
	 *
	 * @param name Name of the area feature
	 * @param description Description of the area feature
	 * @throws IllegalArgumentException if name or description is null or blank
	 */
	public AreaFeature(String name, String description) throws IllegalArgumentException {
		setName(name);
		setDescription(description);
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
	 * @throws IllegalArgumentException if the name is null or blank
	 */
	private void setName(String name) throws IllegalArgumentException {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be null or blank.");
		}
		this.name = name;
	}

	private void setDescription(String description) throws IllegalArgumentException {
		if (description == null || description.isBlank()) {
			throw new IllegalArgumentException("Description cannot be null or blank.");
		}
		this.description = description;
	}

	/**
	 * Updates the description of the area feature.
	 *
	 * @param description Description as String
	 * @throws IllegalArgumentException if the description is null or blank
	 */
	public void updateDescription(String description) throws IllegalArgumentException {
		setDescription(description);
	}

	/**
	 * Sets the areas that have this feature.
	 *
	 * @param areas Areas as Set
	 * @throws IllegalArgumentException if areas is null
	 */
	public void addAreas(Set<Area> areas) throws IllegalArgumentException {
		if (areas == null) {
			throw new IllegalArgumentException("Areas cannot be null.");
		}
		for (Area area : areas) {
			addArea(area);
		}
	}

	/**
	 * Adds an area to the list of areas that have this feature.
	 *
	 * @param area Area to add
	 * @throws IllegalArgumentException if area is null
	 */
	public void addArea(Area area) throws IllegalArgumentException {
		if (area == null) {
			throw new IllegalArgumentException("Area cannot be null.");
		}
		areas.add(area);
	}

	/**
	 * Removes given area from areas set.
	 *
	 * @param area area object to remove
	 * @throws IllegalArgumentException area to remove is null
	 */
	public void removeArea(Area area) throws IllegalArgumentException {
		if (area == null) {
			throw new IllegalArgumentException("Area cannot be null");
		}
		areas.remove(area);
	}
}
