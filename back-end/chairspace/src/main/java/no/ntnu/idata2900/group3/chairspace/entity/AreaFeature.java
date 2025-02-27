package no.ntnu.idata2900.group3.chairspace.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an area feature in the database.
 *
 * @author Odin Lyngsgård
 * @version 1.0
 * @since 0.1
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
	 *
	 * @param name Name of the area feature
	 * @param description Description of the area feature
	 * @throws IllegalArgumentException if name or description is null or blank
	 */
	public AreaFeature(String name, String description) {
		setName(name);
		setDescription(description);
		areas = new HashSet<>();
	}


	/* ---- Getters ---- */

	/**
	 * Returns the name of the area feature.
	 *
	 * @return Name as String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description of the area feature.
	 *
	 * @return Description as String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the areas that have this feature.
	 *
	 * @return Areas as Set
	 */
	public Set<Area> getAreas() {
		return areas;
	}

	/* ---- Setters ---- */

	/**
	 * Sets the name of the area feature.
	 *
	 * @param name Name as String
	 * @throws IllegalArgumentException if name is null or blank
	 */
	private void setName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be null or blank.");
		}
		this.name = name;
	}

	/**
	 * Sets the description of the area feature.
	 *
	 * @param description Description as String
	 * @throws IllegalArgumentException if description is null or blank
	 */
	public void setDescription(String description) {
		if (description == null || description.isBlank()) {
			throw new IllegalArgumentException("Description cannot be null or blank.");
		}
		this.description = description;
	}

	/**
	 * Sets the areas that have this feature.
	 *
	 * @param areas Areas as Set
	 * @throws IllegalArgumentException if areas is null
	 */
	public void addAreas(Set<Area> areas) {
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
	public void addArea(Area area) {
		if (area == null) {
			throw new IllegalArgumentException("Area cannot be null.");
		}
		areas.add(area);
	}

	/**
	 * Removes given area from areas set.
	 *
	 * @param area area object to remove
	 * @throws IllegalArgumentException if given null
	 */
	public void removeArea(Area area) {
		if (area == null) {
			throw new IllegalArgumentException("Area cannot be null");
		}
		areas.remove(area);
	}
}
