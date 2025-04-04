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
import no.ntnu.idata2900.group3.chairspace.exceptions.AdminCountException;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.NotReservableException;
import no.ntnu.idata2900.group3.chairspace.exceptions.ReservedException;
import org.apache.commons.lang3.NotImplementedException;

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
	private boolean reservable;

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
		this.reservations = new HashSet<>();
		this.reservable = builder.reservable;
		this.id = builder.id;
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
	public Iterator<AreaFeature> getFeatures() {
		return features.iterator();
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
	 * Returns true if the area is reservable, false if not.
	 *
	 * @return reservable status
	 */
	public boolean isReservable() {
		return reservable;
	}


	/* ---- Setters ---- */


	/**
	 * Adds a reservation if the area is free for the matching time span.
	 *
	 * @param reservation the reservation to add
	 * @throws ReservedException if area is not free for the reservations timespan
	 * @throws NotReservableException if area is not reservable
	 */
	public void addReservation(Reservation reservation)
		throws  ReservedException, NotReservableException {
		if (reservation == null) {
			throw new IllegalArgumentException("Reservation is null when value was expected ");
		}
		if (!reservable) {
			throw NotReservableException.overlapException();
		}
		if (!isFreeBetween(reservation.getStart(), reservation.getEnd())) {
			throw ReservedException.reservationOverlapException();
		}
		reservations.add(reservation);
	}

	/**
	 * Adds a user to the assigned administrators of this area.
	 *
	 * @param newAdmin the new user to be added to admin list
	 */
	public void addAdministrator(User newAdmin) {
		if (newAdmin == null) {
			throw new IllegalArgumentException("Administrator is null when value was expected");
		}
		administrators.add(newAdmin);
	}

	/**
	 * Removes a administrator from the set of administrators.
	 *
	 * @param toRemove User to remove
	 * @throws AdminCountException If you are trying to remove the last user of the area.
	 */
	public void removeAdministrator(User toRemove) throws AdminCountException {
		if (toRemove == null) {
			throw new IllegalArgumentException("User to remove is null when value was expected");
		}
		if (getAdminCount() <= 1) {
			throw new AdminCountException("Cannot remove the last administrator of a area");
		}
		administrators.remove(toRemove);
	}

	/**
	 * Removes the existing super area.
	 * Will not perform the action if the area has no administrators of it's own.
	 * This is to ensure that a area always has a administrator.
	 *
	 * @throws AdminCountException if area has no administrators of it's own
	 */
	public void removeSuperArea() throws AdminCountException {
		if (getAreaSpecificAdministrators().isEmpty()) {
			throw new AdminCountException(
				"Cannot remove super area as this area has not administrators of it's own"
			);
		}
		this.superArea = null;
	}

	/**
	 * Removes reservation from the area.
	 *
	 * @param reservation the reservation to remove.
	 * @throws IllegalArgumentException if reservation is null
	 */
	public void removeReservation(Reservation reservation)
		throws IllegalArgumentException {
		if (reservation == null) {
			throw new IllegalArgumentException("Reservation is null");
		}
		reservations.remove(reservation);
	}

	/**
	 * Sets the super area of the area.
	 *
	 * @param area Super area
	 * @throws InvalidArgumentCheckedException if this area already has a super area
	 * @throws InvalidArgumentCheckedException If you are trying to assign a area to become
	 *     a super area of itself
	 * @throws AdminCountException if you try to set the super area to null while this area has no
	 *     administrators of itself

	 */
	public void setSuperArea(Area area)
		throws InvalidArgumentCheckedException, AdminCountException {
		if (this.superArea != null) {
			throw new IllegalStateException("This area already has a super area");
		} else if (area == null) {
			removeSuperArea();
		} else if (area.isSuperArea(this) || area == this) {
			throw new InvalidArgumentCheckedException("Cannot make a space it's own superspace");
		} else {
			this.superArea = area;
		}
	}

	/**
	 * Adds a area feature.
	 *
	 * @param areaFeature The feature to add
	 */
	public void addAreaFeature(AreaFeature areaFeature) {
		if (areaFeature == null) {
			throw new IllegalArgumentException("Area feature is null when value is expected");
		}
		features.add(areaFeature);
	}

	/**
	 * Updates the description.
	 *
	 * @param newDescription new description as string.
	 */
	public void updateDescription(String newDescription) {
		if (newDescription == null) {
			throw new IllegalArgumentException("newDescription is null when value was expected");
		}
		description = newDescription;
	}

	/**
	 * Updates the capacity.
	 * Throws an exception if the new capacity is less than 0.
	 *
	 * @param newCapacity the updated capacity
	 * @throws InvalidArgumentCheckedException if newCapacity is less than 0
	 */
	public void updateCapacity(int newCapacity) throws InvalidArgumentCheckedException {
		if (newCapacity < 0) {
			throw new InvalidArgumentCheckedException("Capacity cannot be less than 0");
		}
		capacity = newCapacity;
	}

	/**
	 * Updates the calendar link.
	 * Will set the calendar controlled status to true if a link is provided.
	 *
	 * @param newCalendarLink the new calendar link
	 */
	public void updateCalendarLink(String newCalendarLink) {
		if (newCalendarLink == null || newCalendarLink.isEmpty()) {
			calendarControlled = false;
			calendarLink = null;
		} else {
			calendarControlled = true;
			calendarLink = newCalendarLink;
		}
	}

	/**
	 * Updates the name of the area.
	 *
	 * @param newName the new name of the area
	 * @throws InvalidArgumentCheckedException if newName is null or empty
	 */
	public void updateName(String newName) throws InvalidArgumentCheckedException {
		if (newName == null) {
			throw new InvalidArgumentCheckedException("Name cannot be null");
		}
		if (newName.isBlank()) {
			throw new InvalidArgumentCheckedException("Name cannot be empty");
		}
		name = newName;
	}

	/**
	 * Updates the area type of the area.
	 *
	 * @param newAreaType the new area type
	 * @throws IllegalArgumentException if newAreaType is null
	 */
	public void updateAreaType(AreaType newAreaType) {
		if (newAreaType == null) {
			throw new IllegalArgumentException("Area type is null when value was expected");
		}
		areaType = newAreaType;
	}

	/**
	 * Toggles the reservable state of the area.
	 * State will not change from true to false if there are reservations in the future.
	 *
	 * @param reservable the new reservable state
	 * @throws InvalidArgumentCheckedException if there are reservations in the future when trying
	 *     to make area non reservable
	 */
	public void setReservable(boolean reservable) throws InvalidArgumentCheckedException {
		if (!reservable) {
			Iterator<Reservation> it = getReservations();
			LocalDateTime now = LocalDateTime.now();
			boolean reservationsInFuture = false;
			while (it.hasNext() && !reservationsInFuture) {
				Reservation reservation = it.next();

				reservationsInFuture = reservation.getEnd().isAfter(now);
			}
			if (reservationsInFuture) {
				throw new InvalidArgumentCheckedException(
					"Cannot make area non reservable as there are reservation in the future"
				);
			}
		}
		this.reservable = reservable;
	}

	/* ---- Methods ---- */

	private void isFreeIncludingSubAreas() {
		//TODO: implement
		// Should isFreeBetween check super area for conflicts
		throw new NotImplementedException();
	}

	/**
	 * Returns true if the area is free in this block of time. false if not.
	 *
	 * @param start start of time block
	 * @param end end of time block
	 * @return true or false depending on if reservations exists in this block of time
	 */
	public boolean isFreeBetween(LocalDateTime start, LocalDateTime end)
		throws IllegalArgumentException {
		if (start == null) {
			throw new IllegalArgumentException("Start is null when value was expected");
		}
		if (end == null) {
			throw new IllegalArgumentException("End is null when value was expected");
		}
		// Thanks Sigve
		return reservations.stream()
			.noneMatch(r -> r.doesCollide(start, end));
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
			throw new IllegalArgumentException("Time is null when value was expected");
		}
		return reservations.stream()
			.noneMatch(r -> r.doesCollide(time));
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
		private Set<AreaFeature> features;
		private boolean reservable;
		private UUID id;
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
		public Builder(String name, int capacity, AreaType areaType)
			throws InvalidArgumentCheckedException {
			name(name);
			capacity(capacity);
			areaType(areaType);

			// Default value
			description = "";
			calendarControlled = false;
			administrators = new HashSet<>();
			features = new HashSet<>();
		}

		/* ---- Setters ---- */

		/**
		 * Sets the name of the area.
		 *
		 * @param name The name of the object
		 * @return Builder object
		 * @throws InvalidArgumentCheckedException when name is empty
		 */
		private Builder name(String name) throws InvalidArgumentCheckedException {
			if (name == null) {
				throw new IllegalArgumentException("Name is null when value was expected");
			}
			if (name.isBlank()) {
				throw new InvalidArgumentCheckedException("Name Cannot be empty");
			}
			this.name = name;
			reservable = true;
			return this;
		}

		/**
		 * Sets the capacity of the area as an int. Throws exception if capacity is less than 0.
		 *
		 * @param capacity as int
		 * @return Builder object
		 * @throws InvalidArgumentCheckedException if capacity is less than 0
		 */
		private Builder capacity(int capacity) throws InvalidArgumentCheckedException {
			if (capacity < 0) {
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
		 */
		private Builder areaType(AreaType areaType) {
			if (areaType == null) {
				throw new IllegalArgumentException("AreaType is null when value was expected");
			}
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
			if (description == null) {
				throw new IllegalArgumentException("Description is null when value was expected");
			}
			this.description = description;
			return this;
		}

		/**
		 * Sets the calendar link of the area.
		 *
		 * @param calendarLink as String
		 * @return Builder object
		 * @throws InvalidArgumentCheckedException if calendar link has invalid format
		 */
		public Builder calendarLink(String calendarLink) throws InvalidArgumentCheckedException {
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
			if (administrator == null) {
				throw new IllegalArgumentException("Administrator is null when value was expected");
			}
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
			if (feature == null) {
				throw new IllegalArgumentException("Feature is null when value was expected");
			}
			features.add(feature);
			return this;
		}

		/**
		 * Builds the Area object.
		 *
		 * @return Area object
		 * @throws AdminCountException if build is called without having an assigned administrator
		 * @see Area.Builder#administrator(User)
		 */
		public Area build() throws AdminCountException {
			//If area has no administrators of itself,
			// and super area is either null or has no administrators
			if (administrators.isEmpty() && (superArea == null || superArea.getAdminCount() <= 0)) {
				throw new AdminCountException("Cannot create area without administrator");
			}
			return new Area(this);
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
