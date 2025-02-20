package no.ntnu.idata2900.group3.chairspace.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a reservable area in the database.
 * Implements a builder pattern.
 */
@Entity
@Schema(description = "Represents a reservable area in the database")
@Table(name = "areas")
public class Area {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "area_uuid")
	private UUID id;
	@ManyToMany
	private Set<User> administrators;
	@ManyToOne()
	private Area superArea;
	@ManyToOne
	private AreaType areaType;
	@OneToMany(mappedBy = "superArea")
	private Set<Area> subAreas;
	@OneToMany
	private Set<Reservation> reservations;
	private int capacity;
	private boolean calendarControlled;
	private String calendarLink;
	private String name;
	private String description;

	/**
	 * No args constructor for JPA.
	 */
	public Area() {}

	private Area(Builder builder) {
		this.name = builder.name;
		this.description = builder.description;
		this.capacity = builder.capacity;
		this.superArea = builder.superArea;
		this.areaType = builder.areaType;
		this.calendarControlled = builder.calendarControlled;
		this.calendarLink = builder.calendarLink;

		if (builder.administrators == null) {
			this.administrators = new HashSet<>();
		} else {
			this.administrators = builder.administrators;
		}

		if (builder.subAreas == null) {
			this.subAreas = new HashSet<>();
		} else {
			this.subAreas = builder.subAreas;
		}

		if (builder.reservations == null) {
			this.reservations = new HashSet<>();
		} else {
			this.reservations = builder.reservations;
		}
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
	public Set<User> getAdministrator() {
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

	/**
	 * Returns the capacity of the area as int.
	 *
	 * @return Capacity as int
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Returns the calendar controlled status of the area.
	 *
	 * @return Calendar controlled status as boolean
	 */
	public boolean isCalendarControlled() {
		return calendarControlled;
	}

	/**
	 * Returns the calendar link of the area.
	 *
	 * @return Calendar link as String
	 */
	public String getCalendarLink() {
		return calendarLink;
	}

	/**
	 * Returns the name of the area.
	 *
	 * @return Name as String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description of the area.
	 *
	 * @return Description as String
	 */
	public String getDescription() {
		return description;
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
	private void setAdministrators(Set<User> administrators) {
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

	/**
	 * Builder class for Area.
	 */
	public static class Builder {
		private String name;
		private String description;
		private int capacity;
		private boolean calendarControlled;
		private String calendarLink;
		private Set<User> administrators;
		private Area superArea;
		private AreaType areaType;
		private Set<Area> subAreas;
		private Set<Reservation> reservations;
		// Damn this is a lot of fields.
		// Good im using the builder pattern then 😎

		/**
		 * Constructor.
		 * If calendar link is not set. it will rely on reservation objects.
		 * To get it's reservable status.
		 *
		 * @param name as String
		 * @param capacity int
		 * @param areaType AreaType object
		 * @throws IllegalArgumentException if name is null or empty
		 * @throws IllegalArgumentException if capacity is less than 1
		 * @throws IllegalArgumentException if areaType is null
		 */
		public Builder(String name, int capacity, AreaType areaType) {
			name(name);
			capacity(capacity);
			areaType(areaType);
			// Default value
			calendarControlled = false;

			administrators = new HashSet<>();
			subAreas = new HashSet<>();
			reservations = new HashSet<>();
		}

		/* ---- Setters ---- */

		/**
		 * Sets the name of the area.
		 *
		 * @param name as String
		 * @return Builder object
		 * @throws IllegalArgumentException if name is null or empty
		 */
		private Builder name(String name) {
			if (name == null || name.isEmpty()) {
				throw new IllegalArgumentException("Name is null");
			}
			this.name = name;
			return this;
		}

		/**
		 * Sets the capacity of the area as an int.
		 *
		 * @param capacity as int
		 * @return Builder object
		 * @throws IllegalArgumentException if capacity is less than 1
		 */
		private Builder capacity(int capacity) {
			if (capacity < 1) {
				throw new IllegalArgumentException("Capacity is less than 1");
			}
			this.capacity = capacity;
			return this;
		}

		/**
		 * Sets the area type of the area.
		 *
		 * @param areaType AreaType object
		 * @return Builder object
		 * @throws IllegalArgumentException if areaType is null
		 */
		private Builder areaType(AreaType areaType) {
			if (areaType == null) {
				throw new IllegalArgumentException("AreaType is null");
			}
			this.areaType = areaType;
			return this;
		}

		/**
		 * Sets the description of the area.
		 *
		 * @param description as String
		 * @return Builder object
		 * @throws IllegalArgumentException if description is null
		 */
		public Builder description(String description) {
			if (description == null || description.isEmpty()) {
				throw new IllegalArgumentException("Description is null");
			}
			this.description = description;
			return this;
		}

		/**
		 * Sets the calendar link of the area.
		 *
		 * @param calendarLink as String
		 * @return Builder object
		 * @throws IllegalArgumentException if calendar link is null
		 */
		public Builder calendarLink(String calendarLink) {
			if (calendarLink == null || calendarLink.isEmpty()) {
				throw new IllegalArgumentException("Calendar link is null");
			}
			this.calendarLink = calendarLink;
			this.calendarControlled = true;
			return this;
		}


		/**
		 * Sets the administrators of the area.
		 *
		 * @param administrators Set of User objects
		 * @return Builder object
		 * @throws IllegalArgumentException if administrators is null
		 */
		public Builder administrators(Set<User> administrators) {
			if (administrators == null || administrators.isEmpty()) {
				throw new IllegalArgumentException("Administrators is null");
			}
			for (User user : administrators) {
				if (user != null) {
					administrators.add(user);
				}
			}
			return this;
		}

		/**
		 * Sets the super area of the area.
		 *
		 * @param superArea Area object
		 * @return Builder object
		 * @throws IllegalArgumentException if super area is null
		 */
		public Builder superArea(Area superArea) {
			if (superArea == null) {
				throw new IllegalArgumentException("Super area is null");
			}
			this.superArea = superArea;
			return this;
		}

		/**
		 * Sets the sub areas of the area.
		 *
		 * @param subAreas Set of Area objects
		 * @return Builder object
		 * @throws IllegalArgumentException if sub areas is null
		 */
		public Builder subAreas(Set<Area> subAreas) {
			if (subAreas == null || subAreas.isEmpty()) {
				throw new IllegalArgumentException("Sub areas is null");
			}
			for (Area area : subAreas) {
				if (area != null) {
					subAreas.add(area);
				}
			}
			return this;
		}

		/**
		 * Sets the reservations of the area.
		 *
		 * @param reservations Set of Reservation objects
		 * @return Builder object
		 * @throws IllegalArgumentException if reservations is null
		 */
		public Builder reservations(Set<Reservation> reservations) {
			if (reservations == null || reservations.isEmpty()) {
				throw new IllegalArgumentException("Reservations is null");
			}
			for (Reservation reservation : reservations) {
				if (reservation != null) {
					reservations.add(reservation);
				}
			}
			return this;
		}

		/**
		 * Builds the Area object.
		 *
		 * @return Area object
		 */
		public Area build() {
			return new Area(this);
		}

	}
}
