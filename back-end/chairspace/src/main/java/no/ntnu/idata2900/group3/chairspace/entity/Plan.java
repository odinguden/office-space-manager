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
 * Plans represents a block of time where an area that otherwise is not reservable
 * becomes reservable.
 * Plans are to be checked if an area is marked with PlanControlled.
 */
@Entity
public class Plan implements EntityInterface<UUID> {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@ManyToOne
	private Area area;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;

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
	 * @param startDate The start date of the plan.
	 * @param endDate The end date of the plan.
	 * @throws InvalidArgumentCheckedException if the start date is after the end date.
	 */
	public Plan(String name, Area area, LocalDate startDate, LocalDate endDate)
		throws InvalidArgumentCheckedException {
		setName(name);
		if (startDate.isAfter(endDate)) {
			throw new InvalidArgumentCheckedException();
		}
		setArea(area);
		setStart(startDate);
		setEnd(endDate);
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
	 * @param startDate The start date of the plan.
	 * @throws IllegalArgumentException if the start date is null.
	 */
	private void setStart(LocalDate startDate) {
		if (startDate == null) {
			throw new IllegalArgumentException("Start date cannot be null.");
		}
		this.startDate = startDate;
	}

	/**
	 * Sets the end date of the plan.
	 *
	 * @param endDate The end date of the plan.
	 * @throws IllegalArgumentException if the end date is null.
	 */
	private void setEnd(LocalDate endDate) {
		if (endDate == null) {
			throw new IllegalArgumentException("End date cannot be null.");
		}
		this.endDate = endDate;
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
		return startDate;
	}

	/**
	 * Gets the end date of the plan.
	 *
	 * @return The end date of the plan.
	 */
	public LocalDate getEnd() {
		return endDate;
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