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
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.ReservedException;

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
	public Iterator<Reservation> getReservations() {
		return reservations.iterator();
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
	 * @throws InvalidArgumentCheckedException if reservation is null
	 * @throws ReservedException if area is not free for the reservations timespan
	 */
	public void addReservation(Reservation reservation)
		throws InvalidArgumentCheckedException, ReservedException {
		if (reservation == null) {
			throw new InvalidArgumentCheckedException("Reservation cannot be null");
		}
		if (!isFreeBetween(reservation.getStart(), reservation.getEnd())) {
			throw new ReservedException("Area is not free for the reservations timespan");
		}
		reservations.add(reservation);
	}

	/**
	 * Adds a user to the assigned administrators of this area.
	 *
	 * @param newAdmin the new user to be added to admin list
	 * @throws InvalidArgumentCheckedException if any of the provided arguments are null
	 */
	public void addAdministrator(User newAdmin)	throws InvalidArgumentCheckedException {
		if (newAdmin == null) {
			throw new InvalidArgumentCheckedException();
		}
		administrators.add(newAdmin);
	}

	/**
	 * Removes a administrator from the set of administrators.
	 *
	 * @param toRemove User to remove
	 * @throws IllegalStateException If you are trying to remove the last user of the area.
	 * @throws InvalidArgumentCheckedException if any of the provided arguments are null
	 */
	public void removeAdministrator(User toRemove)
		throws IllegalStateException, InvalidArgumentCheckedException {
		if (toRemove == null) {
			throw new InvalidArgumentCheckedException();
		}
		//TODO: Better exceptions
		if (getAdminCount() == 1) {
			throw new IllegalStateException("Cannot remove the last administrator of a area");
		}
		administrators.remove(toRemove);
	}

	/**
	 * Removes a sub area from this area.
	 *
	 * @param subArea The area to remove
	 * @throws InvalidArgumentCheckedException if subArea is null
	 */
	public void removeSubArea(Area subArea)
		throws InvalidArgumentCheckedException {
		if (subArea == null) {
			throw new InvalidArgumentCheckedException("Sub area cannot be null");
		}
		subAreas.remove(subArea);
	}

	/**
	 * Replaces existing super area with a new area.
	 *
	 * @param area new area
	 * @throws InvalidArgumentCheckedException if subArea is null
	 */
	public void replaceSuperArea(Area area)
		throws InvalidArgumentCheckedException {
		if (area == null) {
			throw new InvalidArgumentCheckedException("Super area cannot be null");
		}
		if (area.getAdminCount() == 0) {
			throw new InvalidArgumentCheckedException("Cannot set area as super area as it has no administrators");
		}
		removeSuperArea();
		setSuperArea(area);
	}

	/**
	 * Removes the existing super area.
	 * Will not perform the action if the area has no administrators of it's own.
	 * This is to ensure that a area always has a administrator.
	 *
	 * @throws IllegalStateException if area has no administrators of it's own
	 */
	public void removeSuperArea() throws IllegalStateException {
		if (getAreaSpecificAdministrators().isEmpty()) {
			//TODO: Better exception
			throw new IllegalStateException(
				"Cannot remove super area as this area has not administrators of it's own"
			);
		}
		this.superArea = null;
	}

	/**
	 * Removes reservation from the area.
	 *
	 * @param reservation the reservation to remove.
	 * @throws InvalidArgumentCheckedException if reservation is null
	 */
	public void removeReservation(Reservation reservation)
		throws InvalidArgumentCheckedException, IllegalStateException {
		if (reservation == null) {
			throw new InvalidArgumentCheckedException("Reservation is null");
		}
		reservations.remove(reservation);
	}

	/**
	 * Sets the super area of the area.
	 *
	 * @param superArea Super area
	 * @throws IllegalStateException if this area already has a super area
	 * @throws InvalidArgumentCheckedException if super area is null
	 */
	public void setSuperArea(Area superArea)
		throws InvalidArgumentCheckedException, IllegalStateException {
		if (this.superArea != null) {
			throw new IllegalStateException("This area already has a super area");
		}
		if (superArea == null) {
			throw new InvalidArgumentCheckedException("Cannot set super area to null.");
		}
		this.superArea = superArea;
	}

	/**
	 * Adds a area feature.
	 *
	 * @param areaFeature The feature to add
	 * @throws InvalidArgumentCheckedException if areaFeature is null
	 */
	public void addAreaFeature(AreaFeature areaFeature) throws InvalidArgumentCheckedException {
		if (areaFeature == null) {
			throw new InvalidArgumentCheckedException();
		}
		if (!features.contains(areaFeature)) {
			features.add(areaFeature);
		}
	}

	/**
	 * Updates the description.
	 *
	 * @param newDescription new description as string.
	 * @throws InvalidArgumentCheckedException if newDescription is null or blank
	 */
	public void updateDescription(String newDescription) throws InvalidArgumentCheckedException {
		if (newDescription == null || newDescription.isBlank()) {
			throw new InvalidArgumentCheckedException("New description does not contain any information");
		}
		description = newDescription;
	}

	/**
	 * Updates the capacity.
	 * Throws an exception if the new capacity is less than 1.
	 *
	 * @param newCapacity the updated capacity
	 * @throws InvalidArgumentCheckedException if newCapacity is less than 1
	 */
	public void updateCapacity(int newCapacity) throws InvalidArgumentCheckedException {
		if (newCapacity <= 0) {
			throw new InvalidArgumentCheckedException("Capacity cannot be less than 1");
		}
		capacity = newCapacity;
	}

	/**
	 * Adds a single sub area to this area.
	 *
	 * @param area the area to add
	 * @param user the user preforming the operation
	 * @throws InvalidArgumentCheckedException if null arguments are provided
	 * @throws IllegalStateException If user is not a administrator of this area.
	 *     Or if the area that is being set as sub area already has a sub area
	 */
	public void addSubArea(Area area, User user)
		throws InvalidArgumentCheckedException, IllegalStateException {
		if (area == null || user == null) {
			throw new InvalidArgumentCheckedException("Null argument provided");
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
	 * @throws InvalidArgumentCheckedException if start or end is null
	 */
	public boolean isFreeBetween(LocalDateTime start, LocalDateTime end)
		throws InvalidArgumentCheckedException {
		if (start == null || end == null) {
			throw new InvalidArgumentCheckedException("Null argument provided");
		}
		boolean isFree = true;
		Iterator<Reservation> it = getReservations();
		while (isFree && it.hasNext()) {
			Reservation reservation = it.next();
			if (end.isBefore(reservation.getEnd()) && end.isAfter(reservation.getStart())) {
				//Timespan ends during reservation
				isFree = false;
			}
			if (start.isAfter(reservation.getStart()) && start.isBefore(reservation.getEnd())) {
				//Timespan starts during reservation
				isFree = false;
			}
			if (reservation.getStart().isAfter(start) && reservation.getEnd().isBefore(end)) {
				//Reservation is during timespan
				isFree = false;
			}
		}
		return isFree;
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
		boolean isFree = true;
		Iterator<Reservation> it = getReservations();
		while (isFree && it.hasNext()) {
			Reservation reservation = it.next();
			if (time.isBefore(reservation.getEnd()) && time.isAfter(reservation.getStart())) {
				isFree = false;
			}
		}
		return isFree;
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
		 * @throws InvalidArgumentCheckedException if name is null or empty
		 * @throws InvalidArgumentCheckedException if capacity is less than 1
		 * @throws InvalidArgumentCheckedException if areaType is null
		 */
		public Builder(String name, int capacity, AreaType areaType)
			throws InvalidArgumentCheckedException {
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
		 * @throws InvalidArgumentCheckedException if name is null or empty
		 */
		private Builder name(String name) throws InvalidArgumentCheckedException {
			if (name == null || name.isEmpty()) {
				throw new InvalidArgumentCheckedException("Name is null");
			}
			this.name = name;
			return this;
		}

		/**
		 * Sets the capacity of the area as an int. Throws exception if capacity is less than 1.
		 *
		 * @param capacity as int
		 * @return Builder object
		 * @throws InvalidArgumentCheckedException if capacity is less than 1
		 */
		private Builder capacity(int capacity) throws InvalidArgumentCheckedException {
			if (capacity < 1) {
				throw new InvalidArgumentCheckedException("Capacity is less than 1");
			}
			this.capacity = capacity;
			return this;
		}

		/**
		 * Sets the area type of the area.
		 *
		 * @param areaType AreaType object
		 * @return Builder object
		 * @throws InvalidArgumentCheckedException if areaType is null
		 */
		private Builder areaType(AreaType areaType) throws InvalidArgumentCheckedException {
			if (areaType == null) {
				throw new InvalidArgumentCheckedException("AreaType is null");
			}
			this.areaType = areaType;
			return this;
		}

		/**
		 * Sets the description of the area.
		 *
		 * @param description as String
		 * @return Builder object
		 * @throws InvalidArgumentCheckedException if description is null
		 */
		public Builder description(String description) throws InvalidArgumentCheckedException {
			if (description == null || description.isEmpty()) {
				throw new InvalidArgumentCheckedException("Description is null");
			}
			this.description = description;
			return this;
		}

		/**
		 * Sets the calendar link of the area.
		 *
		 * @param calendarLink as String
		 * @return Builder object
		 * @throws InvalidArgumentCheckedException if calendar link is null
		 */
		public Builder calendarLink(String calendarLink) throws InvalidArgumentCheckedException {
			if (calendarLink == null || calendarLink.isEmpty()) {
				throw new InvalidArgumentCheckedException("Calendar link is null");
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
		 * @throws InvalidArgumentCheckedException if administrators is null
		 */
		public Builder administrators(Set<User> administrators) throws InvalidArgumentCheckedException {
			if (administrators == null) {
				throw new InvalidArgumentCheckedException("Administrators is null");
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
		 * @throws InvalidArgumentCheckedException if administrators is null
		 */
		public Builder administrator(User administrator) throws InvalidArgumentCheckedException {
			if (administrator == null) {
				throw new InvalidArgumentCheckedException("Administrator is null");
			}
			administrators.add(administrator);
			return this;
		}

		/**
		 * Sets the super area of the area.
		 *
		 * @param superArea Area object
		 * @return Builder object
		 * @throws InvalidArgumentCheckedException if super area is null
		 */
		public Builder superArea(Area superArea) throws InvalidArgumentCheckedException {
			if (superArea == null) {
				throw new InvalidArgumentCheckedException("Super area is null");
			}
			this.superArea = superArea;
			return this;
		}

		/**
		 * Sets the sub areas of the area.
		 *
		 * @param subAreas Set of Area objects
		 * @return Builder object
		 * @throws InvalidArgumentCheckedException if sub areas is null
		 * @throws IllegalStateException If one of the areas already has a super area
		 */
		public Builder subAreas(Set<Area> subAreas) throws InvalidArgumentCheckedException {
			if (subAreas == null) {
				throw new InvalidArgumentCheckedException("Sub areas is null");
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
		 * @throws InvalidArgumentCheckedException if sub area is null
		 */
		public Builder subArea(Area subArea) throws InvalidArgumentCheckedException {
			if (subArea == null) {
				throw new InvalidArgumentCheckedException("Sub area is null");
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
		 * @throws InvalidArgumentCheckedException if features is null
		 */
		public Builder features(Set<AreaFeature> features) throws InvalidArgumentCheckedException {
			if (features == null) {
				throw new InvalidArgumentCheckedException("Features is null");
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
		 * @throws InvalidArgumentCheckedException if feature is null
		 */
		public Builder feature(AreaFeature feature) throws InvalidArgumentCheckedException {
			if (feature == null) {
				throw new InvalidArgumentCheckedException("Feature is null");
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
