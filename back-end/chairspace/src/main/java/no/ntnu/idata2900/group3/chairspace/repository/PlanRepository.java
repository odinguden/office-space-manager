package no.ntnu.idata2900.group3.chairspace.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for the Plan entity.
 */
public interface PlanRepository extends JpaRepository<Plan, UUID> {

	/**
	 * Returns the id of all plan controlled areas that could have a reservation in this timespan.
	 * This method says noting about if a reservation exists in this timespan only that one could.
	 *
	 * @param start start time of search
	 * @param end end-time of search
	 * @return all reservable plan areas
	 */
	@Query("""
			SELECT DISTINCT plan.area.id
			FROM Plan plan
			WHERE plan.area.planControlled = true
			AND plan.area.reservable = true
			AND plan.startTime <= ?1
			AND plan.endTime >= ?2
		""")
	List<UUID> getReservablePlanAreas(LocalDate start, LocalDate end);

	/**
	 * Gets all plans belonging to a certain area.
	 *
	 * @param areaId the id of the area
	 * @param pageable the pageable used for pagination
	 * @return all plans belonging to the area with the given id
	 */
	@Query("SELECT plan FROM Plan plan WHERE plan.area.id = ?1")
	Page<Plan> getPlansByArea(UUID areaId, Pageable pageable);

	/**
	 * Returns true if there is a plan marking the area as free for the given time slot.
	 *
	 * @param areaId the id of the area to check
	 * @param start the start of the time slot to check
	 * @param end the end of the time slot to check
	 * @return true if a plan exists to free the area for the period
	 */
	@Query("""
		SELECT EXISTS(
			SELECT 1
			FROM Plan plan
			WHERE plan.area.id = ?1
			AND plan.start <= ?2
			AND plan.end >= ?3
		)
		""")
	public boolean isReservable(UUID areaId, LocalDate start, LocalDate end);
}
