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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a reservable area in the database.
 * Implements a builder pattern.
 *
 * @author Odin Lyngsgård
 * @version 0.1
 * @since 0.1
 */
@Entity
@Schema(description = "Represents a reservable area in the database")
@Table(name = "areas")
public class Area {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "area_id")
	private UUID id;
	@ManyToMany
	@JoinTable(
		name = "area_administrators",
		joinColumns = {
			@JoinColumn(name = "area_id")
		},
		inverseJoinColumns = {
			@JoinColumn(name = "user_id")
		}
	)
	@Column(name = "administrators")
	private Set<User> administrators;
	@ManyToOne
	private Area superArea;
	@ManyToOne
	private AreaType areaType;
	@OneToMany(mappedBy = "superArea")
	private Set<Area> subAreas;
	@OneToMany(mappedBy = "area")
	private Set<Reservation> reservations;
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


	private static String authErrMessage = "User is not an administrator for this area";

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
		this.subAreas = builder.subAreas;
		this.features = builder.features;
		this.reservations = new HashSet<>();
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
	public Set<User> getAdministrators() {
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
	 * Gets area features.
	 *
	 *  @return area features
	 */
	public Set<AreaFeature> getFeatures() {
		return features;
	}

	/**
	 * Gets reservations in a set.
	 *
	 * @return reservations
	 */
	public Set<Reservation> getReservations() {
		return reservations;
	}

	/**
	 * Gets sub areas of this area.
	 *
	 * @return sub areas in a set
	 */
	public Set<Area> getSubAreas() {
		return subAreas;
	}

	/**
	 * Returns the number of admin users for this area + super areas.
	 * This method is used to ensure that a area always has a admin user.
	 *
	 * @return number of administrators as int
	 */
	public int getAdminCount() {
		int count = administrators.size();

		if (superArea != null) {
			// Behold Sigve the += operator.
			// Are you not proud!!
			count += superArea.getAdminCount();
		}
		return count;
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
	 * Adds a user to the assigned administrators of this area.
	 * Can only be done by an existing administrator.
	 *
	 * @param newAdmin the new user to be added to admin list
	 * @param existingAdmin existing admin preforming the operation
	 */
	public void addAdministrator(User newAdmin, User existingAdmin) {
		if (newAdmin == null || existingAdmin == null) {
			throw new IllegalArgumentException();
		}
		if (!isAdmin(existingAdmin)) {
			throw new IllegalStateException(authErrMessage);
		}
		administrators.add(newAdmin);
	}

	/**
	 * Removes a administrator from set of administrators.
	 *
	 * @param toRemove User to remove
	 * @param existingAdmin user preforming operation
	 * @throws IllegalStateException if user is not administrator, or if user is trying to remove
	 *     the last administrator of the area
	 * @throws IllegalArgumentException if any of the provided arguments are null
	 */
	public void removeAdministrator(User toRemove, User existingAdmin) {
		if (toRemove == null || existingAdmin == null) {
			throw new IllegalArgumentException();
		}
		if (!isAdmin(existingAdmin)) {
			throw new IllegalStateException(authErrMessage);
		}
		if (getAdminCount() == 1) {
			throw new IllegalStateException("Cannot remove the last administrator of a area");
		}
		administrators.remove(toRemove);
	}

	/**
	 * Sets the super area of the area.
	 *
	 * @param superArea Super area
	 * @param user Preforming the action
	 */
	public void setSuperArea(Area superArea, User user) {
		if (!isAdmin(user)) {
			throw new IllegalStateException("User does not have the necessary privileges");
		}
		this.superArea = superArea;
	}

	/**
	 * Adds a area feature.
	 * Must be done by a administrator of the area.
	 *
	 * @param areaFeature The feature to add
	 * @param user user executing the operation
	 */
	public void addAreaFeature(AreaFeature areaFeature, User user) {
		if (areaFeature == null || user == null) {
			throw new IllegalArgumentException();
		}
		if (!isAdmin(user)) {
			throw new IllegalStateException(authErrMessage);
		}
		if (!features.contains(areaFeature)) {
			features.add(areaFeature);
		}
	}

	/**
	 * Updates the description.
	 * Must be done by an administrator of the area.
	 *
	 * @param newDescription new description as string.
	 * @param user user executing the operation.
	 */
	public void updateDescription(String newDescription, User user) {
		if (newDescription == null || newDescription.isBlank()) {
			throw new IllegalArgumentException("New description does not contain any information");
		}
		if (!isAdmin(user) || user == null) {
			throw new IllegalStateException(authErrMessage);
		}
		description = newDescription;
	}

	/**
	 * Updates the capacity if done by a authorized user.
	 *
	 * @param newCapacity the updated capacity
	 * @param user user preforming the action
	 */
	public void updateCapacity(int newCapacity, User user) {
		if (newCapacity <= 0 || user == null) {
			throw new IllegalArgumentException("Null argument provided");
		}
		if (!isAdmin(user)) {
			throw new IllegalStateException(authErrMessage);
		}
		capacity = newCapacity;
	}

	/**
	 * Adds a single sub area to this area.
	 *
	 * @param area the area to add
	 * @param user the user preforming the operation
	 * @throws IllegalArgumentException if null arguments are provided
	 * @throws IllegalStateException If user is not a administrator of this area.
	 *     Or if the area that is being set as sub area already has a sub area
	 */
	public void addSubArea(Area area, User user) {
		if (area == null || user == null) {
			throw new IllegalArgumentException("Null argument provided");
		}
		if (!isAdmin(user)) {
			throw new IllegalStateException(authErrMessage);
		}
		if (area.getSuperArea() != null) {
			throw new IllegalStateException(
				"Cannot add area as sub area if it already has a super area"
			);
		}
		subAreas.add(area);
	}

	/* ---- Methods ---- */

	/**
	 * Returns true if user is administrator of this area or this areas super area.
	 *
	 * @param user User to check
	 * @return true if user is administrator of this area or this areas super area
	 */
	public boolean isAdmin(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User is null when a value was expected");
		}
		return administrators.contains(user) || (superArea != null && superArea.isAdmin(user));
	}

	/**
	 * Builder class for Area.
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
		private Set<Area> subAreas;
		private Set<AreaFeature> features;
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
			features = new HashSet<>();
			subAreas = new HashSet<>();
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
		 * Adds a single administrator to the area.
		 *
		 * @param administrator Single user
		 * @return Builder object
		 * @throws IllegalArgumentException if administrators is null
		 */
		public Builder administrator(User administrator) {
			if (administrator == null) {
				throw new IllegalArgumentException("Administrator is null");
			}
			administrators.add(administrator);
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
		 * @throws IllegalStateException If one of the areas already has a super area
		 */
		public Builder subAreas(Set<Area> subAreas) {
			if (subAreas == null) {
				throw new IllegalArgumentException("Sub areas is null");
			}
			for (Area area : subAreas) {
				subArea(area);
			}
			return this;
		}

		/**
		 * Adds a single sub area to the area.
		 *
		 * @param subArea Area object
		 * @return Builder object
		 * @throws IllegalArgumentException if sub area is null
		 */
		public Builder subArea(Area subArea) {
			if (subArea == null) {
				throw new IllegalArgumentException("Sub area is null");
			}
			if (subArea.getSuperArea() != null) {
				throw new IllegalStateException(
					"Cannot set area as sub area, as it already has a super area"
				);
			}
			subAreas.add(subArea);
			return this;
		}

		/**
		 * Sets the features of the area.
		 *
		 * @param features Set of AreaFeature objects
		 * @return Builder object
		 * @throws IllegalArgumentException if features is null
		 */
		public Builder features(Set<AreaFeature> features) {
			if (features == null || features.isEmpty()) {
				throw new IllegalArgumentException("Features is null");
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
		 * @throws IllegalArgumentException if feature is null
		 */
		public Builder feature(AreaFeature feature) {
			if (feature == null) {
				throw new IllegalArgumentException("Feature is null");
			}
			features.add(feature);
			return this;
		}

		/**
		 * Builds the Area object.
		 *
		 * @return Area object
		 * @throws IllegalStateException if build is called without having an assigned administrator
		 */
		public Area build() {
			if (superArea == null && administrators.isEmpty()) {
				throw new IllegalStateException("Cannot create area without administrator");
			}
			return new Area(this);
		}

	}
}
