package no.ntnu.idata2900.group3.chairspace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;

/**
 * Represents a plan for a area.
 * A plan is a unit of time where a otherwise unreservable area is reservable.
 */
@Entity
public class Plan implements EntityInterface<UUID> {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@ManyToOne
	private Area area;
	private String name;
	private LocalDate startTime;
	private LocalDate endTime;

	/**
	 * No args constructor for JPA.
	 */
	public Plan() {
		// No args constructor for JPA.
	}

	/**
	 * Constructor for Plan.
	 *
	 * @param name The name of the plan.
	 * @param area the area of the plan
	 * @param startTime The start date of the plan.
	 * @param endTime The end date of the plan.
	 * @throws InvalidArgumentCheckedException if the start date is after the end date.
	 */
	public Plan(String name, Area area, LocalDate startTime, LocalDate endTime)
		throws InvalidArgumentCheckedException {
		setName(name);
		if (startTime.isAfter(endTime)) {
			throw new InvalidArgumentCheckedException();
		}
		setArea(area);
		setStart(startTime);
		setEnd(endTime);
	}


	/* ---- Setters ---- */

	/**
	 * Sets the name of the plan.
	 *
	 * @param name The name of the plan.
	 * @throws IllegalArgumentException if the name is null or blank.
	 */
	private void setName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be null or blank.");
		}
		this.name = name;
	}

	/**
	 * Sets the area of the plan.
	 *
	 * @param area the area to set
	 * @throws InvalidArgumentCheckedException if the area is not plan controlled
	 */
	private void setArea(Area area) throws InvalidArgumentCheckedException {
		if (area == null) {
			throw new IllegalArgumentException("Area is null when value was expected");
		}
		if (!area.isPlanControlled()) {
			throw new InvalidArgumentCheckedException(
				"Cannot create plan for non plan controlled area"
			);
		}
		this.area = area;
	}

	/**
	 * Sets the start date of the plan.
	 *
	 * @param startTime The start date of the plan.
	 * @throws IllegalArgumentException if the start date is null.
	 */
	public void setStart(LocalDate startTime) {
		if (startTime == null) {
			throw new IllegalArgumentException("Start date cannot be null.");
		}
		this.startTime = startTime;
	}

	/**
	 * Sets the end date of the plan.
	 *
	 * @param endTime The end date of the plan.
	 * @throws IllegalArgumentException if the end date is null.
	 */
	public void setEnd(LocalDate endTime) {
		if (endTime == null) {
			throw new IllegalArgumentException("End date cannot be null.");
		}
		this.endTime = endTime;
	}

	/* ---- Getters ---- */

	@Override
	public UUID getId() {
		return id;
	}

	/**
	 * Gets the name of the plan.
	 *
	 * @return The name of the plan.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the start date of the plan.
	 *
	 * @return The start date of the plan.
	 */
	public LocalDate getStart() {
		return startTime;
	}

	/**
	 * Gets the end date of the plan.
	 *
	 * @return The end date of the plan.
	 */
	public LocalDate getEnd() {
		return endTime;
	}

	/**
	 * Returns the area of this plan.
	 *
	 * @return the area of this plan
	 */
	public Area getArea() {
		return area;
	}

}
