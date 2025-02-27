package no.ntnu.idata2900.group3.chairspace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a type that an area can be.
 *
 * @author Odin Lyngsgård
 * @version 1.0
 * @since 0.1
 */
@Entity
public class AreaType {
	@Id
	private String name;
	private String description;
	@OneToMany(mappedBy = "areaType")
	private Set<Area> areas;

	/**
	 * No args constructor for JPA.
	 */
	public AreaType() {}

	/**
	 * Constructor for AreaType.
	 *
	 * @param name of the area type
	 * @param description of the area type
	 */
	public AreaType(String name, String description) {
		setName(name);
		setDescription(description);
		areas = new HashSet<>();
	}

	/* ---- Getters ---- */

	/**
	 * Gets the name of the area type.
	 *
	 * @return name as string
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description of the area type.
	 *
	 * @return description as string
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the areas of the area type.
	 *
	 * @return areas as set
	 */
	public Set<Area> getAreas() {
		return areas;
	}

	/* ---- Setters ---- */

	/**
	 * Gets the name of the area type as string.
	 *
	 * @param name of the area type
	 */
	private void setName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be null or empty");
		}
		this.name = name;
	}

	/**
	 * Gets description as string.
	 *
	 * @param description of area type as string.
	 */
	public void setDescription(String description) {
		if (description == null || description.isEmpty()) {
			throw new IllegalArgumentException("Description cannot be null or empty");
		}
		this.description = description;
	}

	/**
	 * Sets the areas of the area type.
	 *
	 * @param areas as set
	 * @throws IllegalArgumentException if set is null
	 */
	public void addAreas(Set<Area> areas) {
		if (areas == null) {
			throw new IllegalArgumentException("Areas cannot be null");
		}
		for (Area area : areas) {
			addArea(area);
		}
	}

	/**
	 * Adds an area to the area type.
	 *
	 * @param area to add
	 */
	public void addArea(Area area) {
		if (area == null) {
			throw new IllegalArgumentException("Area cannot be null");
		}
		areas.add(area);
	}

	/**
	 * Removes an area from the area type.
	 *
	 * @param area to remove
	 */
	public void removeArea(Area area) {
		if (area == null) {
			throw new IllegalArgumentException("Area cannot be null");
		}
		areas.remove(area);
	}
}
