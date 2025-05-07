package no.ntnu.idata2900.group3.chairspace.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Plan;

/**
 * A simplified container for {@link Plan}.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimplePlan(
	UUID id,
	UUID areaId,
	String areaName,
	LocalDate start,
	LocalDate end,
	String name
) {

	/**
	 * Builder for simple plan record.
	 */
	public static class Builder {
		UUID id;
		UUID areaId;
		String areaName;
		LocalDate start;
		LocalDate end;
		String name;

		/**
		 * builds a simple plan from the information contained within a plan.
		 *
		 * @param plan the plan to use as base
		 * @return simple plan built from plan
		 */
		public static final Builder fromPlan(Plan plan) {
			return new Builder()
				.id(plan.getId())
				.areaId(plan.getArea().getId())
				.areaName(plan.getArea().getName())
				.start(plan.getStart())
				.end(plan.getEnd())
				.name(plan.getName());
		}

		/**
		 * Builds a simple plan based on the data stored in the builder class.
		 *
		 * @return the new simple plan record
		 */
		public SimplePlan build() {
			return new SimplePlan(
				id,
				areaId,
				areaName,
				start,
				end,
				name
			);
		}

		/**
		 * The id of the original plan.
		 *
		 * @param id the id of the original plan
		 * @return builder object
		 */
		public Builder id(UUID id) {
			this.id = id;
			return this;
		}

		/**
		 * Sets the area id of the area the plan belongs to.
		 *
		 * @param areaId the area id
		 * @return builder object
		 */
		public Builder areaId(UUID areaId) {
			this.areaId = areaId;
			return this;
		}

		/**
		 * Sets the areaName of this builder.
		 *
		 * @param areaName the areaName to set
		 * @return this builder
		 */
		public Builder areaName(String areaName) {
			this.areaName = areaName;
			return this;
		}

		/**
		 * Sets the start date of the plan.
		 *
		 * @param start the start date of the plan
		 * @return builder object
		 */
		public Builder start(LocalDate start) {
			this.start = start;
			return this;
		}

		/**
		 * Sets the end date of the plan.
		 *
		 * @param end the end date of the plan
		 * @return builder object
		 */
		public Builder end(LocalDate end) {
			this.end = end;
			return this;
		}

		/**
		 * Sets the name of the plan.
		 *
		 * @param name the name of the plan
		 * @return builder object
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}
	}
}
