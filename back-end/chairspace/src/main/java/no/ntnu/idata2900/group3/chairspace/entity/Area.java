package no.ntnu.idata2900.group3.chairspace.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

/**
 *
 */
@Entity
@Schema(description = "Represents a reservable area in the database")
public class Area {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private User administrator;
	@ManyToOne
	private Area superArea;
	@ManyToOne
	private AreaType areaType;

	private Area() {}

	public Area(int id, User administrator, Area superArea, AreaType areaType) {
		setId(id);
		setAdministrator(administrator);
		setSuperArea(superArea);
		setAreaType(areaType);
	}

	/* ---- Getters ---- */


	public int getId() {
		return id;
	}

	public User getAdministrator() {
		return administrator;
	}

	public Area getSuperArea() {
		return superArea;
	}

	public AreaType getAreaType() {
		return areaType;
	}

	/* ---- Setters ---- */


	private void setId(int id) {
		this.id = id;
	}

	private void setAdministrator(User administrator) {
		this.administrator = administrator;
	}

	private void setSuperArea(Area superArea) {
		this.superArea = superArea;
	}

	private void setAreaType(AreaType areaType) {
		this.areaType = areaType;
	}
}
