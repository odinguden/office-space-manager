package no.ntnu.idata2900.group3.chairspace.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Plan;
import no.ntnu.idata2900.group3.chairspace.repository.PlanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with and managing {@link Plan}s.
 *
 * @author Odin Lyngsgård
 */
@Service
public class PlanService extends EntityService<Plan, UUID> {
	private final PlanRepository planRepository;

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
	 * Returns if a plan exists to free the area for the given time slot.
	 *
	 * @param areaId the area to check
	 * @param start the start of the time slot
	 * @param end the end of the time slot
	 * @return true if a plan exists
	 */
	public boolean isFree(UUID areaId, LocalDateTime start, LocalDateTime end) {
		if (start == null || end == null) {
			throw new IllegalArgumentException("Null argument provided when value was expected");
		}
		LocalDate startDate = start.toLocalDate();
		LocalDate endDate = end.toLocalDate();

		return planRepository.isReservable(areaId, startDate, endDate);
	}

	/**
	 * Gets all the plans belonging to a specific area.
	 *
	 * @param areaId the id of the area
	 * @return plans belonging to a area
	 */
	public List<Plan> getPlansByArea(UUID areaId) {
		return planRepository.findByAreaId(areaId);
	}

	/**
	 * Gets all plans by a list of areas as a page.
	 *
	 * @param areaIds the areas to get plans for
	 * @param page the page of plans to get
	 * @return a page of plans belonging to the areas
	 */
	public Page<Plan> getPlansByAreas(List<UUID> areaIds, Integer page) {
		if (page == null || page < 0) {
			page = 0;
		}
		Pageable paging = PageRequest.of(page, DEFAULT_PAGE_SIZE);
		return planRepository.findByAreaIdIn(areaIds, paging);
	}
}
