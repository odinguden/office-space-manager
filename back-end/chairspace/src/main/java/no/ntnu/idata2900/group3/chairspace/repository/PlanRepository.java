package no.ntnu.idata2900.group3.chairspace.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Plan;
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
	 * @return all plans belonging to the area with the given id
	 */
	@Query("SELECT plan FROM Plan plan WHERE plan.area.id = ?1")
	List<Plan> getPlansByArea(UUID areaId);
}
