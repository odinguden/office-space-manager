package no.ntnu.idata2900.group3.chairspace.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.Set;

/**
 * Represents an area feature in the database.
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
	private void setDescription(String description) {
		if (description == null || description.isBlank()) {
			throw new IllegalArgumentException("Description cannot be null or blank.");
		}
		this.description = description;
	}
}
