package no.ntnu.idata2900.group3.chairspace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents a type that an area can be.
 */
@Entity
public class AreaType {
	@Id
	private String name;
	private String description;

	public AreaType(int id, String name, String description) {

	}


	/* ---- Getters ---- */

	/**
	 * Gets the name of the area type
	 * @return name as string
	 */
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	/* ---- Setters ---- */

	/**
	 * Gets the name of the area type as string.
	 *
	 * @param name of the area type
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets description as string.
	 *
	 * @param description of area type as string.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
