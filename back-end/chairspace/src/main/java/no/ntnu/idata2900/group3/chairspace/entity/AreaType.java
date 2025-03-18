package no.ntnu.idata2900.group3.chairspace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a type that an area can be.
 * Contains the name and description of the area type.
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
	 * @throws InvalidArgumentCheckedException if name or description is null or blank
	 */
	public AreaType(String name, String description) throws InvalidArgumentCheckedException {
		setName(name);
		setDescription(description);
		areas = new HashSet<>();
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

	/**
	 * Gets the areas that are of this type.
	 *
	 * @return the areas that have this area type in a set
	 */
	public Set<Area> getAreas() {
		return areas;
	}

	/* ---- Setters ---- */

	/**
	 * Gets the name of the area type as string.
	 *
	 * @param name of the area type
	 * @throws InvalidArgumentCheckedException if name is null or empty
	 */
	private void setName(String name) throws InvalidArgumentCheckedException {
		if (name == null || name.isEmpty()) {
			throw new InvalidArgumentCheckedException("Name cannot be null or empty");
		}
		this.name = name;
	}

	private void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Updates the description .
	 *
	 * @param description the new description of the area type.
	 * @throws InvalidArgumentCheckedException if description is null or empty
	 */
	public void updateDescription(String description) throws InvalidArgumentCheckedException {
		if (description == null || description.isEmpty()) {
			throw new InvalidArgumentCheckedException("Description cannot be null or empty");
		}
		setDescription(description);
	}

	/**
	 * Adds multiple areas to the area type.
	 *
	 * @param areas as set
	 * @throws InvalidArgumentCheckedException if set is null
	 */
	public void addAreas(Set<Area> areas) throws InvalidArgumentCheckedException {
		if (areas == null) {
			throw new InvalidArgumentCheckedException("Areas cannot be null");
		}
		for (Area area : areas) {
			addArea(area);
		}
	}

	/**
	 * Adds an area to the area type.
	 *
	 * @param area the area to add
	 * @throws InvalidArgumentCheckedException if area is null
	 */
	public void addArea(Area area) throws InvalidArgumentCheckedException {
		if (area == null) {
			throw new InvalidArgumentCheckedException("Area cannot be null");
		}
		areas.add(area);
	}

	/**
	 * Removes an area from the area type.
	 *
	 * @param area the area to remove
	 * @throws InvalidArgumentCheckedException if area is null
	 */
	public void removeArea(Area area) throws InvalidArgumentCheckedException {
		if (area == null) {
			throw new InvalidArgumentCheckedException("Area cannot be null");
		}
		areas.remove(area);
	}
}
