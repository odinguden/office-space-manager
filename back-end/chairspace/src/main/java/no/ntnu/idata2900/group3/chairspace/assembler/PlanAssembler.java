package no.ntnu.idata2900.group3.chairspace.assembler;

import no.ntnu.idata2900.group3.chairspace.dto.SimplePlan;
import no.ntnu.idata2900.group3.chairspace.dto.SimplePlan.Builder;
import no.ntnu.idata2900.group3.chairspace.entity.Plan;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.service.AreaService;
import org.springframework.stereotype.Component;

/**
 * A utility class that helps convert Plan entity to and from it's respective dto.
 */
@Component
public class PlanAssembler {
	private AreaService areaService;

	/**
	 * Creates new instance of PlanAssembler.
	 *
	 * @param areaService area service
	 */
	public PlanAssembler(AreaService areaService) {
		this.areaService = areaService;
	}

	/**
	 * Builds a simple plan from a plan entity.
	 *
	 * @param plan the plan to build the simple plan from
	 * @return the simple plan
	 */
	public SimplePlan toSimplePlan(Plan plan) {
		return Builder.fromPlan(plan).build();
	}

	/**
	 * Converts a simple plan to a plan entity.
	 *
	 * @param simplePlan the simple plan to convert
	 * @return plan entity
	 * @throws InvalidArgumentCheckedException if any of the arguments are invalid
	 */
	public Plan toDomain(SimplePlan simplePlan) throws InvalidArgumentCheckedException {
		return new Plan(
			simplePlan.name(),
			areaService.get(simplePlan.areaId()),
			simplePlan.start(),
			simplePlan.end()
		);
	}
}
