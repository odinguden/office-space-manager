package no.ntnu.idata2900.group3.chairspace.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import no.ntnu.idata2900.group3.chairspace.entity.User;

/**
 * A simplified container for {@link Area}.
 *
 * @author Sigve Bjørkedal
 * @see Area
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimpleArea(
	UUID id,
	String name,
	String description,
	Set<UUID> administratorIds,
	List<SimpleArea> superAreas,
	AreaType areaType,
	Set<AreaFeature> areaFeatures,
	Integer capacity,
	String calendarLink,
	Boolean reservable,
	SimpleReservationList reservations
) {
	/**
	 * A builder for simple areas.
	 */
	public static class Builder {
		private UUID id;
		private String name;
		private String description;
		private Set<UUID> administratorIds;
		private List<SimpleArea> superAreas;
		private AreaType areaType;
		private Set<AreaFeature> areaFeatures;
		private Integer capacity;
		private String calendarLink;
		private Boolean reservable;
		private SimpleReservationList reservations;

		/**
		 * Creates a new builder and prefills it with information from the input Area.
		 *
		 * @param area the area to project
		 * @return a builder containing properties from area.
		 */
		public static final Builder fromArea(Area area) {
			return new Builder()
				.id(area.getId())
				.name(area.getName())
				.description(area.getDescription())
				.administrators(area.getAdministrators())
				.addSuperAreaRecursive(area)
				.areaType(area.getAreaType())
				.areaFeatures(area.getFeatures())
				.capacity(area.getCapacity())
				.calendarLink(area.getCalendarLink())
				.reservable(area.isReservable());
		}

		/**
		 * Creates a new builder for {@link SimpleArea}s.
		 */
		public Builder() {
			this.superAreas = new ArrayList<>();
		}

		/**
		 * Builds a new simple area from this builder.
		 *
		 * @return a new simple area based on values in this builder.
		 */
		public SimpleArea build() {
			return new SimpleArea(
				this.id,
				this.name,
				this.description,
				this.administratorIds,
				this.superAreas,
				this.areaType,
				this.areaFeatures,
				this.capacity,
				this.calendarLink,
				this.reservable,
				this.reservations
			);
		}

		/**
		 * Sets the id of this builder.
		 *
		 * @param id the id to set
		 * @return this builder
		 */
		public Builder id(UUID id) {
			this.id = id;
			return this;
		}

		/**
		 * Sets the name of this builder.
		 *
		 * @param name the name to set
		 * @return this builder
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * Sets the description of this builder.
		 *
		 * @param description the description to set
		 * @return this builder
		 */
		public Builder description(String description) {
			this.description = description;
			return this;
		}

		/**
		 * Sets the administratorIds of this builder from a set of users.
		 *
		 * @param administrators a set of admin users
		 * @return this builder
		 */
		public Builder administrators(Set<User> administrators) {
			this.administratorIds(
				administrators.stream()
					.map(User::getId)
					.collect(Collectors.toSet())
			);
			return this;
		}

		/**
		 * Sets the administratorIds of this builder.
		 *
		 * @param administratorIds the administratorIds to set
		 * @return this builder
		 */
		public Builder administratorIds(Set<UUID> administratorIds) {
			this.administratorIds = administratorIds;
			return this;
		}

		/**
		 * Adds an area as a super area to this area.
		 *
		 * @param area the area to add as a super area
		 * @return this builder
		 */
		public Builder addSuperArea(Area area) {
			SimpleArea simpleSuperArea = new Builder()
				.areaType(area.getAreaType())
				.name(area.getName())
				.id(area.getId())
				.build();

			this.superAreas.add(simpleSuperArea);

			return this;
		}

		/**
		 * Adds all super areas of the input (excluding the input) as super areas.
		 *
		 * @param baseArea the area whose super areas to recursively add
		 * @return this builder
		 */
		public Builder addSuperAreaRecursive(Area baseArea) {
			// We add this extra logic to ensure we don't infinitely loop in the case of a
			// cycle.
			Set<UUID> visited = new HashSet<>();

			Area area = baseArea.getSuperArea();

			while (area != null) {
				if (!visited.add(area.getId())) {
					System.out.println(
						"Cycle detected in super area hierarchy. Responsible id: "
						+ area.getId().toString()
					);
					// Exit the loop safely
					break;
				}
				this.addSuperArea(area);
				area = area.getSuperArea();
			}

			return this;
		}

		/**
		 * Sets the areaType of this builder.
		 *
		 * @param areaType the areaType to set
		 * @return this builder
		 */
		public Builder areaType(AreaType areaType) {
			this.areaType = areaType;
			return this;
		}

		/**
		 * Sets the areaFeatures of this builder.
		 *
		 * @param areaFeatures the areaFeatures to set
		 * @return this builder
		 */
		public Builder areaFeatures(Set<AreaFeature> areaFeatures) {
			this.areaFeatures = areaFeatures;
			return this;
		}

		/**
		 * Sets the capacity of this builder.
		 *
		 * @param capacity the capacity to set
		 * @return this builder
		 */
		public Builder capacity(Integer capacity) {
			this.capacity = capacity;
			return this;
		}

		/**
		 * Sets the calendarLink of this builder.
		 *
		 * @param calendarLink the calendarLink to set
		 * @return this builder
		 */
		public Builder calendarLink(String calendarLink) {
			this.calendarLink = calendarLink;
			return this;
		}

		/**
		 * Sets the reservable of this builder.
		 *
		 * @param reservable the reservable to set
		 * @return this builder
		 */
		public Builder reservable(Boolean reservable) {
			this.reservable = reservable;
			return this;
		}

		/**
		 * Sets the reservations of this builder.
		 *
		 * @param reservations the reservations to set
		 * @return this builder
		 */
		public Builder reservations(SimpleReservationList reservations) {
			this.reservations = reservations;
			return this;
		}
	}
}
