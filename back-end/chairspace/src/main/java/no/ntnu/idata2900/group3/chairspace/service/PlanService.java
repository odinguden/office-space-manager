package no.ntnu.idata2900.group3.chairspace.service;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Plan;
import no.ntnu.idata2900.group3.chairspace.repository.PlanRepository;

/**
 * Service class for interacting with and managing {@link Plan}s.
 *
 * @author Odin Lyngsgård
 */
public class PlanService extends EntityService<Plan, UUID> {

	/**
	 * Creates a new plan service.
	 *
	 * @param repository autowired PlanRepository
	 */
	public PlanService(PlanRepository repository) {
		super(repository);
	}
}
