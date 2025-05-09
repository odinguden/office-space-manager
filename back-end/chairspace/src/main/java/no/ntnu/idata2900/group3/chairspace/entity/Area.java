package no.ntnu.idata2900.group3.chairspace.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.exceptions.AdminCountException;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;

/**
 * Represents a reservable area in the database.
 * contains information about the area, such as name, description, capacity, and features.
 *
 * <p>
 * Implements a builder pattern.
 *
 * @author Odin Lyngsgård
 * @version 0.2
 * @since 0.1
 */
@Entity
@Schema(description = "Represents a reservable area in the database")
@Table(name = "areas")
public class Area implements EntityInterface<UUID> {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "area_id")
	private UUID id;
	@ManyToMany
	@JoinTable(
		name = "area_administrators",
		joinColumns = @JoinColumn(name = "area_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	@Column(name = "administrators")
	private Set<User> administrators;
	@ManyToOne
	private Area superArea;
	@ManyToOne
	private AreaType areaType;
	private int capacity;
	private boolean calendarControlled;
	private String calendarLink;
	private String name;
	private String description;
	@ManyToMany
	@JoinTable(
		name = "area_features",
		joinColumns = {
			@JoinColumn(name = "area_id")
		},
		inverseJoinColumns = {
			@JoinColumn(name = "feature_id")
		}
	)
	private Set<AreaFeature> features;
	private boolean reservable;
	private boolean planControlled = false;

	/**
	 * No args constructor for JPA.
	 */
	public Area() {}

	/**
	 * Constructor for Area.
	 * Uses a builder pattern.
	 *
	 * @param builder Builder object
	 */
	private Area(Builder builder) {
		this.name = builder.name;
		this.description = builder.description;
		this.capacity = builder.capacity;
		this.superArea = builder.superArea;
		this.areaType = builder.areaType;
		this.calendarControlled = builder.calendarControlled;
		this.calendarLink = builder.calendarLink;
		this.administrators = builder.administrators;
		this.features = builder.features;
		this.reservable = builder.reservable;
		this.id = builder.id;
		this.planControlled = builder.isPlanControlled;
	}

	/* ---- Getters ---- */


	/**
	 * Returns the id of the area.
	 *
	 * @return the id of the area
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Returns the administrators of the area.
	 *
	 * <p>
	 * Includes administrators from superArea if one exists.
	 *
	 * @return Administrators of area, including super area.
	 */
	public Set<User> getAdministrators() {
		Set<User> allAdmins = new HashSet<>(administrators);
		if (getSuperArea() != null) {
			allAdmins.addAll(superArea.getAdministrators());
		}
		return allAdmins;
	}

	/**
	 * Gets the administrators that exist only for this area.
	 *
	 * <p>
	 * Does not include administrators from superArea
	 *
	 * @return administrators specifically for this area.
	 */
	public Set<User> getAreaSpecificAdministrators() {
		return administrators;
	}

	/**
	 * Returns the super area of the area.
	 * Returns null if no super area exists.
	 *
	 * @return the super area of this area. Null if no super area exists.
	 */
	public Area getSuperArea() {
		return superArea;
	}


	/**
	 * Returns the area type of the area. (e.g. Meeting room, office space)
	 *
	 * @return Area type of the area
	 * @see AreaType
	 */
	public AreaType getAreaType() {
		return areaType;
	}

	/**
	 * Returns the capacity of the area as int.
	 *
	 * @return the capacity of the area
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Returns the calendar controlled status of the area.
	 *
	 * @return True if calendar controlled, false if not.
	 */
	public boolean isCalendarControlled() {
		return calendarControlled;
	}

	/**
	 * Returns the calendar link of the area.
	 *
	 * @return The calendar link of the area. null if area is not calendar controlled.
	 */
	public String getCalendarLink() {
		return calendarLink;
	}

	/**
	 * Returns the name of the area as a string.
	 *
	 * @return The name of the area
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the features of the area in a set.
	 *
	 * @return The features of this area
	 * @see AreaFeature
	 */
	public Set<AreaFeature> getFeatures() {
		return features;
	}

	/**
	 * Returns the number of admin users for this area + super areas.
	 * This method is used to ensure that a area always has a admin user.
	 *
	 * @return the number of admin users for this area + super areas
	 */
	public int getAdminCount() {
		return getAdministrators().size();
	}

	/**
	 * Returns the description of the area as a string.
	 *
	 * @return the description of the area
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Checks if a area exists as a super area of this area.
	 *
	 * @param area Area to check
	* @return true if area exists as super of this area
	 */
	public boolean isSuperArea(Area area) {
		// If super area is null, return false as this is the top of the stack.
		// If super area is also the area we're checking return true.
		// if there is a super area but its not the area we're checking for check the super
		//    for that area
		return superArea != null && (superArea == area || superArea.isSuperArea(area));
	}

	/**
	 * Checks if a area exists as a super area of this area.
	 *
	 * @param areaId id of area to check
	* @return true if area exists as super of this area
	 */
	public boolean isSuperArea(UUID areaId) {
		return superArea != null && (superArea.getId() == areaId || superArea.isSuperArea(areaId));
	}

	/**
	 * Returns the plan controlled status of the area.
	 *
	 * @return true if plan controlled.
	 */
	public boolean isPlanControlled() {
		return planControlled;
	}

	/**
	 * Returns true if the area is reservable, false if not.
	 *
	 * @return reservable status
	 */
	public boolean isReservable() {
		return reservable;
	}

	/* ---- Methods ---- */

	/**
	 * Returns true if user is administrator of this area or this areas super area.
	 *
	 * @param user User to check
	 * @return true if user is administrator of this area or this areas super area
	 * @throws IllegalArgumentException if user is null
	 */
	public boolean isAdmin(User user)
		throws IllegalArgumentException {
		if (user == null) {
			throw new IllegalArgumentException("User is null when a value was expected");
		}
		return getAdministrators().contains(user);
	}

	/**
	 * Builder class for Area.
	 * Implements a builder pattern.
	 *
	 * <p>
	 * Requires name, capacity, and areaType to be set.
	 * Other fields are optional and can be set using the method that matches the field name.
	 * The build method must be called to create the Area object.
	 *
	 * @author Odin Lyngsgård
	 * @version 0.1
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
		private Set<AreaFeature> features;
		private boolean reservable;
		private UUID id;
		private boolean isPlanControlled;
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
		 * @throws InvalidArgumentCheckedException if capacity is less than 1
		 */
		public Builder(String name, int capacity, AreaType areaType) {
			name(name);
			capacity(capacity);
			areaType(areaType);

			// Default values
			description = "";
			calendarControlled = false;
			isPlanControlled = false;
			administrators = new HashSet<>();
			features = new HashSet<>();
		}

		/**
		 * Builds the Area object.
		 * Also validates the object before building.
		 *
		 * @return Area object
		 * @throws AdminCountException if build is called without having an assigned administrator
		 * @throws InvalidArgumentCheckedException if name is empty
		 * @throws InvalidArgumentCheckedException if capacity is less than 0
		 * @throws IllegalArgumentException if name is null
		 * @throws IllegalArgumentException if areaType is null
		 * @throws IllegalArgumentException if administrator is null
		 * @see Area.Builder#administrator(User)
		 */
		public Area build() throws AdminCountException, InvalidArgumentCheckedException {
			// Validate the object before building
			if (name == null) {
				throw new IllegalArgumentException("Name is null when value was expected");
			}
			if (name.isBlank()) {
				throw new InvalidArgumentCheckedException("Name Cannot be empty");
			}
			if (capacity < 0) {
				throw new InvalidArgumentCheckedException("Capacity is less than 0");
			}
			if (areaType == null) {
				throw new IllegalArgumentException("AreaType is null when value was expected");
			}
			if (description == null) {
				description = "";
			}
			boolean hasNullUser = administrators.stream().anyMatch(Objects::isNull);
			if (hasNullUser) {
				throw new IllegalArgumentException("Administrator is null when value was expected");
			}
			boolean hasNullFeature = features.stream().anyMatch(Objects::isNull);
			if (hasNullFeature) {
				throw new IllegalArgumentException("Area feature is null when value was expected");
			}
			//If area has no administrators of itself,
			// and super area is either null or has no administrators
			if (administrators.isEmpty() && (superArea == null || superArea.getAdminCount() <= 0)) {
				throw new AdminCountException("Cannot create area without administrator");
			}

			if (superArea != null && this.id != null && superArea.isSuperArea(this.id)) {
				throw new IllegalStateException("Area cannot be super area of itself");
			}
			return new Area(this);
		}

		/* ---- Setters ---- */

		/**
		 * Sets the name of the area.
		 *
		 * @param name The name of the object
		 * @return Builder object
		 */
		private Builder name(String name) {
			this.name = name;
			reservable = true;
			return this;
		}

		/**
		 * Sets the capacity of the area as an int. Throws exception if capacity is less than 0.
		 *
		 * @param capacity as int
		 * @return Builder object
		 */
		private Builder capacity(int capacity) {
			this.capacity = capacity;
			return this;
		}

		/**
		 * Sets the area type of the area.
		 *
		 * @param areaType AreaType object
		 * @return Builder object
		 */
		private Builder areaType(AreaType areaType) {
			this.areaType = areaType;
			return this;
		}

		/**
		 * Sets the description of the area.
		 *
		 * @param description as String
		 * @return Builder object
		 */
		public Builder description(String description) {
			this.description = description;
			return this;
		}

		/**
		 * Sets the calendar link of the area.
		 *
		 * @param calendarLink as String
		 * @return Builder object
		 */
		public Builder calendarLink(String calendarLink) {
			if (calendarLink == null || calendarLink.isEmpty()) {
				this.calendarLink = null;
			} else {
				//TODO When implementing proper calendar handling, include link format check here :)
				this.calendarLink = calendarLink;
				this.calendarControlled = true;
			}
			return this;
		}


		/**
		 * Sets the administrators of the area.
		 *
		 * @param administrators Set of User objects
		 * @return Builder object
		 */
		public Builder administrators(Set<User> administrators) {
			if (administrators == null) {
				throw new IllegalArgumentException(
					"Administrators is null when value was expected"
				);
			}
			for (User user : administrators) {
				administrator(user);
			}
			return this;
		}

		/**
		 * Adds a single administrator to the area.
		 *
		 * <p>
		 * If {@link #build()} is called without having an administrator set,
		 * an exception will be thrown.
		 *
		 * @param administrator Single user
		 * @return Builder object
		 */
		public Builder administrator(User administrator)  {
			administrators.add(administrator);
			return this;
		}

		/**
		 * Sets the super area of the area.
		 *
		 * @param superArea Area object
		 * @return Builder object
		 */
		public Builder superArea(Area superArea) {
			this.superArea = superArea;
			return this;
		}

		/**
		 * Sets the features of the area.
		 *
		 * @param features Set of AreaFeature objects
		 * @return Builder object
		 */
		public Builder features(Set<AreaFeature> features) {
			if (features == null) {
				throw new IllegalArgumentException("Features is null when value was expected");
			}
			for (AreaFeature feature : features) {
				feature(feature);
			}
			return this;
		}

		/**
		 * Adds a single feature to the area.
		 *
		 * @param feature AreaFeature object
		 * @return Builder object
		 */
		public Builder feature(AreaFeature feature) {
			features.add(feature);
			return this;
		}

		/**
		 * Sets the id of the area that is to be built.
		 * This parameter is nullable
		 *
		 * @param id id as UUID
		 * @return builder object
		 */
		public Builder id(UUID id) {
			this.id = id;
			return this;
		}

		/**
		 * Sets the plan controlled status of the area.
		 * Will be false by default.
		 *
		 * @param planControlled the plan controlled status of the area.
		 * @return builder object
		 */
		public Builder isPlanControlled(Boolean planControlled) {
			if (planControlled == null) {
				this.isPlanControlled = false;
			} else {
				this.isPlanControlled = planControlled;
			}
			return this;
		}

		/**
		 * Sets the reservable state of the area.
		 * Will be true be default
		 *
		 * @param reservable the reservable state of the area.
		 * @return builder object
		 */
		public Builder reservable(Boolean reservable) {
			this.reservable = reservable;
			return this;
		}
	}
}
