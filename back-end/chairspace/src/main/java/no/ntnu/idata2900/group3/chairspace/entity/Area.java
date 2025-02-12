package no.ntnu.idata2900.group3.chairspace.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a reservable area in the database.
 *
 * @TODO Implement factory pattern for creating areas.
 */
@Entity
@Schema(description = "Represents a reservable area in the database")
public class Area {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@ManyToMany
	private Set<Account> administrators;
	@ManyToOne()
	private Area superArea;
	@ManyToOne
	private AreaType areaType;
	@OneToMany(mappedBy = "superArea")
	private Set<Area> subAreas;
	@OneToMany
	private Set<Reservation> reservations;

	/**
	 * No args constructor for JPA.
	 */
	public Area() {}

	/**
	 * Args constructor for Area.
	 *
	 * @param administrator Set of administrators
	 * @param superArea Super area
	 * @param areaType Area type
	 */
	public Area(Set<Account> administrator, Area superArea, AreaType areaType) {
		setAdministrators(administrator);
		setSuperArea(superArea);
		setAreaType(areaType);
	}

	/* ---- Getters ---- */


	/**
	 * Returns the id of the area.
	 *
	 * @return Id as UUID
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Returns the administrators of the area.
	 *
	 * @return Set of administrators
	 */
	public Set<Account> getAdministrator() {
		return administrators;
	}

	/**
	 * Returns the super area of the area.
	 *
	 * @return Super area
	 */
	public Area getSuperArea() {
		return superArea;
	}

	/**
	 * Returns the area type of the area.
	 *
	 * @return Area type
	 */
	public AreaType getAreaType() {
		return areaType;
	}

	/* ---- Setters ---- */

	/**
	 * Sets the id of the area.
	 *
	 * @param id as UUID
	 */
	private void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Sets the administrators of the area.
	 *
	 * @param administrators Set of administrators
	 */
	private void setAdministrators(Set<Account> administrators) {
		this.administrators = administrators;
	}

	/**
	 * Sets the super area of the area.
	 *
	 * @param superArea Super area
	 */
	private void setSuperArea(Area superArea) {
		this.superArea = superArea;
	}

	/**
	 * Sets the area type of the area.
	 *
	 * @param areaType Area type
	 */
	private void setAreaType(AreaType areaType) {
		this.areaType = areaType;
	}
}
