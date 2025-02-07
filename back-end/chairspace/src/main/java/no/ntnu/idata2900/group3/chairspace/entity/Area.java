package no.ntnu.idata2900.group3.chairspace.entity;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 *
 */
@Entity
@Schema(description = "Represents a reservable area in the database")
public class Area {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
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

	private Area() {}

	public Area(int id, Set<Account> administrator, Area superArea, AreaType areaType) {
		setId(id);
		setAdministrators(administrator);
		setSuperArea(superArea);
		setAreaType(areaType);
	}

	/* ---- Getters ---- */


	public int getId() {
		return id;
	}

	public Set<Account> getAdministrator() {
		return administrators;
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

	private void setAdministrators(Set<Account> administrators) {
		this.administrators = administrators;
	}

	private void setSuperArea(Area superArea) {
		this.superArea = superArea;
	}

	private void setAreaType(AreaType areaType) {
		this.areaType = areaType;
	}
}
