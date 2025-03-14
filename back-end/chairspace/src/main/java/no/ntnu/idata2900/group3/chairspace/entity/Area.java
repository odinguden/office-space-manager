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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a reservable area in the database.
 * contains information about the area, such as name, description, capacity, and features.
 *
 * <p>
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
		Set<User> allAdmins = new HashSet<>();
		allAdmins.addAll(administrators);
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
	 * Returns the reservations of this area in a set.
	 *
	 * @return the reservations of this area.
	 * @see Reservation
	 */
	public Set<Reservation> getReservations() {
		return reservations;
	}

	/**
	 * Returns the sub areas of this area in a set.
	 *
	 * @return the sub areas of this area
	 */
	public Set<Area> getSubAreas() {
		return subAreas;
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


	/* ---- Setters ---- */


	/**
	 * Adds a reservation if the area is free for the matching time span.
	 *
	 * @param reservation the reservation to add
	 * @throws IllegalArgumentException if reservation is null
	 * @throws IllegalStateException if area is not free for the reservations timespan
	 */
	public void addReservation(Reservation reservation)
		throws IllegalArgumentException, IllegalStateException {
		if (reservation == null) {
			throw new IllegalArgumentException("Reservation cannot be null");
		}
		if (!isFreeBetween(reservation.getStart(), reservation.getEnd())) {
			throw new IllegalStateException("Area is not free for the reservations timespan");
		}
		reservations.add(reservation);
	}

	/**
	 * Adds a user to the assigned administrators of this area.
	 * Can only be done by an existing administrator.
	 *
	 * @param newAdmin the new user to be added to admin list
	 * @param existingAdmin existing admin preforming the operation
	 * @throws IllegalArgumentException if any of the provided arguments are null
	 * @throws IllegalStateException if user is not an administrator of this area
	 */
	public void addAdministrator(User newAdmin, User existingAdmin)
		throws IllegalArgumentException, IllegalStateException {
		if (newAdmin == null || existingAdmin == null) {
			throw new IllegalArgumentException();
		}
		if (!isAdmin(existingAdmin)) {
			throw new IllegalStateException(authErrMessage);
		}
		administrators.add(newAdmin);
	}

	/**
	 * Removes a administrator from the set of administrators.
	 *
	 * @param toRemove User to remove
	 * @param existingAdmin user preforming operation
	 * @throws IllegalStateException if user is not administrator, or if user is trying to remove
	 *     the last administrator of the area
	 * @throws IllegalArgumentException if any of the provided arguments are null
	 */
	public void removeAdministrator(User toRemove, User existingAdmin)
		throws IllegalStateException, IllegalArgumentException {
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
	 * Removes a sub area from this area.
	 * Will not perform the action if user does not have the correct credentials.
	 *
	 * @param subArea The area to remove
	 * @param user the user preforming the action
	 * @throws IllegalArgumentException if subArea is null
	 * @throws IllegalStateException if user is not an administrator of this area
	 */
	public void removeSubArea(Area subArea, User user)
		throws IllegalStateException, IllegalArgumentException {
		if (subArea == null) {
			throw new IllegalArgumentException("Sub area cannot be null");
		}
		if (!isAdmin(user)) {
			throw new IllegalStateException(authErrMessage);
		}
		subAreas.remove(subArea);
	}

	/**
	 * Replaces existing super area with a new area.
	 *
	 * @param area new area
	 * @param user user preforming the action
	 * @throws IllegalArgumentException if subArea is null
	 * @throws IllegalStateException if user is not an administrator of this area
	 */
	public void replaceSuperArea(Area area, User user)
		throws IllegalArgumentException, IllegalStateException {
		removeSuperArea(user);
		setSuperArea(area, user);
	}

	/**
	 * Removes the existing super area.
	 * Will not perform the action if the area has no administrators of it's own.
	 * This is to ensure that a area always has a administrator.
	 *
	 * @param user the user preforming the action
	 * @throws IllegalStateException if user is not an administrator of this area
	 * @throws IllegalStateException if area has no administrators of it's own
	 */
	public void removeSuperArea(User user) throws IllegalStateException {
		if (!isAdmin(user)) {
			throw new IllegalStateException(authErrMessage);
		}
		if (getAreaSpecificAdministrators().isEmpty()) {
			throw new IllegalStateException(
				"Cannot remove super area as this area has not administrators of it's own"
			);
		}
		this.superArea = null;
	}

	/**
	 * Removes reservation if the user preforming the action is either a administrator of this area,
	 * or the owner of the reservation.
	 *
	 * @param reservation the reservation to remove.
	 * @param user the user preforming the action
	 * @throws IllegalArgumentException if reservation is null
	 * @throws IllegalStateException
	 *      if user is not an administrator of this area, or the owner of the reservation
	 */
	public void removeReservation(Reservation reservation, User user)
		throws IllegalArgumentException, IllegalStateException {
		if (reservation == null) {
			throw new IllegalArgumentException("Reservation is null");
		}
		if (!isAdmin(user) && reservation.getUser() != user) {
			throw new IllegalStateException(authErrMessage);
		}
		reservations.remove(reservation);
	}

	/**
	 * Sets the super area of the area.
	 *
	 * @param superArea Super area
	 * @param user Preforming the action
	 * @throws IllegalStateException if user is not an administrator of this area
	 * @throws IllegalStateException if this area already has a super area
	 * @throws IllegalArgumentException if super area is null
	 */
	public void setSuperArea(Area superArea, User user)
		throws IllegalArgumentException, IllegalStateException {
		if (!isAdmin(user)) {
			throw new IllegalStateException("User does not have the necessary privileges");
		}
		if (this.superArea != null) {
			throw new IllegalStateException("This area already has a super area");
		}
		if (superArea == null) {
			throw new IllegalArgumentException("Cannot set super area to null.");
		}
		this.superArea = superArea;
	}

	/**
	 * Adds a area feature.
	 * Must be done by a administrator of the area.
	 *
	 * @param areaFeature The feature to add
	 * @param user user executing the operation
	 * @throws IllegalArgumentException if areaFeature is null
	 * @throws IllegalStateException if user is not an administrator of this area
	 */
	public void addAreaFeature(AreaFeature areaFeature, User user)
		throws IllegalArgumentException, IllegalStateException {
		if (areaFeature == null) {
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
	 * @throws IllegalArgumentException if newDescription is null or blank
	 * @throws IllegalStateException if user is not an administrator of this area
	 */
	public void updateDescription(String newDescription, User user)
		throws IllegalArgumentException, IllegalStateException {
		if (newDescription == null || newDescription.isBlank()) {
			throw new IllegalArgumentException("New description does not contain any information");
		}
		if (!isAdmin(user)) {
			throw new IllegalStateException(authErrMessage);
		}
		description = newDescription;
	}

	/**
	 * Updates the capacity if done by a authorized user.
	 *
	 * @param newCapacity the updated capacity
	 * @param user user preforming the action
	 * @throws IllegalArgumentException if newCapacity is less than 1
	 * @throws IllegalArgumentException if user is null
	 * @throws IllegalStateException if user is not an administrator of this area
	 */
	public void updateCapacity(int newCapacity, User user)
		throws IllegalArgumentException, IllegalStateException {
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
	public void addSubArea(Area area, User user)
		throws IllegalArgumentException, IllegalStateException {
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
	 * Returns true if the area is free in this block of time. false if not.
	 *
	 * @param start start of time block
	 * @param end end of time block
	 * @return true or false depending on if reservations exists in this block of time
	 * @throws IllegalArgumentException if start or end is null
	 */
	public boolean isFreeBetween(LocalDateTime start, LocalDateTime end)
		throws IllegalArgumentException {
		if (start == null || end == null) {
			throw new IllegalArgumentException("Null argument provided");
		}
		for (Reservation reservation : reservations) {
			if (end.isBefore(reservation.getEnd()) && end.isAfter(reservation.getStart())) {
				//Timespan ends during reservation
				return false;
			}
			if (start.isAfter(reservation.getStart()) && start.isBefore(reservation.getEnd())) {
				//Timespan starts during reservation
				return false;
			}
			if (reservation.getStart().isAfter(start) && reservation.getEnd().isBefore(end)) {
				//Reservation is during timespan
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if the area is free at this time. false if not.
	 *
	 * @param time The time to check
	 * @return True or false depending on if the point of time is free
	 * @throws IllegalArgumentException if time is null
	 */
	public boolean isFree(LocalDateTime time)
		throws IllegalArgumentException {
		if (time == null) {
			throw new IllegalArgumentException("Null argument provided");
		}
		for (Reservation reservation : reservations) {
			if (time.isBefore(reservation.getEnd()) && time.isAfter(reservation.getStart())) {
				return false;
			}
		}
		return true;
	}

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
		public Builder(String name, int capacity, AreaType areaType)
			throws IllegalArgumentException {
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
		 * @param name The name of the object
		 * @return Builder object
		 * @throws IllegalArgumentException if name is null or empty
		 */
		private Builder name(String name) throws IllegalArgumentException {
			if (name == null || name.isEmpty()) {
				throw new IllegalArgumentException("Name is null");
			}
			this.name = name;
			return this;
		}

		/**
		 * Sets the capacity of the area as an int. Throws exception if capacity is less than 1.
		 *
		 * @param capacity as int
		 * @return Builder object
		 * @throws IllegalArgumentException if capacity is less than 1
		 */
		private Builder capacity(int capacity) throws IllegalArgumentException {
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
		private Builder areaType(AreaType areaType) throws IllegalArgumentException {
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
		public Builder description(String description) throws IllegalArgumentException {
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
		public Builder calendarLink(String calendarLink) throws IllegalArgumentException {
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
		public Builder administrators(Set<User> administrators) throws IllegalArgumentException {
			if (administrators == null) {
				throw new IllegalArgumentException("Administrators is null");
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
		 * @throws IllegalArgumentException if administrators is null
		 */
		public Builder administrator(User administrator) throws IllegalArgumentException {
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
		public Builder superArea(Area superArea) throws IllegalArgumentException {
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
		public Builder subAreas(Set<Area> subAreas) throws IllegalArgumentException {
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
		public Builder subArea(Area subArea) throws IllegalArgumentException {
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
		public Builder features(Set<AreaFeature> features) throws IllegalArgumentException {
			if (features == null) {
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
		public Builder feature(AreaFeature feature) throws IllegalArgumentException {
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
		 * @see Area.Builder#administrator(User)
		 */
		public Area build() throws IllegalStateException {
			if (superArea == null && administrators.isEmpty()) {
				throw new IllegalStateException("Cannot create area without administrator");
			}
			return new Area(this);
		}
	}
}
