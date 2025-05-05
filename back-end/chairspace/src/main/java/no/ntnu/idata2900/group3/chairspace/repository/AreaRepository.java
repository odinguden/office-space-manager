package no.ntnu.idata2900.group3.chairspace.repository;

import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Repository for the Area entity.
 */
@Repository
public interface AreaRepository extends JpaRepository<Area, UUID> {
	/**
	 * Finds all areas by whether they are reservable.
	 *
	 * @param reservable whether to get reservable or non-reservable areas
	 * @return a list of areas by whether they are reservable
	 */
	@Query("""
		SELECT area
		FROM Area area
		WHERE area.reservable = :reservable
		AND area.planControlled = false
		""")
	List<Area> findAllByReservable(boolean reservable);

	/**
	 * Finds all direct sub areas of a given super area.
	 *
	 * @param superAreaId id of the super area to find sub areas for
	 * @return a list of sub areas of the given super area
	 */
	List<UUID> findIdBySuperAreaId(UUID superAreaId);

	/**
	 * Searches for areas based on various optional parameters.
	 *
	 * @param pageable the pageable used for pagination
	 * @param capacity the minimum capacity of the area
	 * @param subAreaIds the list of sub area IDs to filter by
	 * @param areaTypeId area type ID to filter by
	 * @param areaFeatureIds the list of area feature IDs to filter by
	 * @param freeAreaIds the list of free area IDs to filter by
	 * @param featureCount the minimum number of features the area should have
	 * @return a list of areas that match the search criteria
	 */
	@Query(value = """
		SELECT area
		FROM Area area
		LEFT JOIN area.features areaFeature
		WHERE area.reservable = true
		AND (:freeAreas IS NULL OR area.id IN (:freeAreas))
		AND (:subAreas IS NULL OR area.id IN (:subAreas))
		AND (:capacity IS NULL OR area.capacity >= :capacity)
		AND (:areaType IS NULL OR area.areaType.name = :areaType)
		AND (:features IS NULL OR areaFeature.name IN (:features))
		GROUP BY area
		HAVING (:featureCount IS NULL OR COUNT(areaFeature) >= :featureCount)
		""")
	Page<Area> searchWithOptionalParams(
		Pageable pageable,
		@Param("capacity") Integer capacity,
		@Param("subAreas") List<UUID> subAreaIds,
		@Param("areaType") String areaTypeId,
		@Param("features") List<String> areaFeatureIds,
		@Param("freeAreas") List<UUID> freeAreaIds,
		@Param("featureCount") Integer featureCount
	);

	/**
	 * Finds all areas that have this user as an admin.
	 *
	 * @param userId the id of user to find areas for
	 * @param pageable the pageable used for pagination
	 * @return a list of areas that have this user as an admin
	 */
	@Query("SELECT area FROM Area area JOIN area.administrators admin WHERE admin.id = :userId")
	Page<Area> findByAdmin(UUID userId, Pageable pageable);
}
