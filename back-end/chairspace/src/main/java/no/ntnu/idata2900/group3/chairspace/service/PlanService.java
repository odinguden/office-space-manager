package no.ntnu.idata2900.group3.chairspace.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Plan;
import no.ntnu.idata2900.group3.chairspace.repository.PlanRepository;

/**
 * Service class for interacting with and managing {@link Plan}s.
 *
 * @author Odin Lyngsgård
 */
public class PlanService extends EntityService<Plan, UUID> {
	PlanRepository planRepository;

	/**
	 * Creates a new plan service.
	 *
	 * @param repository autowired PlanRepository
	 */
	public PlanService(PlanRepository repository) {
		super(repository);
		this.planRepository = repository;
	}

	/**
	 * Returns the id of all plan controlled areas that are reservable in this block of time.
	 * The areas might have a reservation in this
	 *
	 * @param start the start time
	 * @param end the end time
	 * @return the id of all plan controlled areas that are reservable
	 */
	public List<UUID> getFreePlanAreas(LocalDateTime start, LocalDateTime end) {
		return planRepository.getReservablePlanAreas(start.toLocalDate(), end.toLocalDate());
	}

	/**
	 * Gets all the plans belonging to a specific area.
	 *
	 * @param areaId the id of the area
	 * @return plans belonging to a area
	 */
	public List<Plan> getPlansByArea(UUID areaId) {
		return planRepository.getPlansByArea(areaId);
	}
}
