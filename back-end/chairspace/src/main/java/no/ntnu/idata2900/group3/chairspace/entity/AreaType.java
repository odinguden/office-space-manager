package no.ntnu.idata2900.group3.chairspace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;

/**
 * Represents a type that an area can be.
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

	/* ---- Setters ---- */

	/**
	 * Gets the name of the area type as string.
	 *
	 * @param name of the area type
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets description as string.
	 *
	 * @param description of area type as string.
	 */
	private void setDescription(String description) {
		this.description = description;
	}
}
