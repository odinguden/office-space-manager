package no.ntnu.idata2900.group3.chairspace.repository;

import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Repository for the Area entity.
 */
@Repository
public interface AreaRepository extends CrudRepository<Area, UUID> {
	/**
	 * Finds all areas by whether they are reservable.
	 *
	 * @param reservable whether to get reservable or non-reservable areas
	 * @return a list of areas by whether they are reservable
	 */
	Iterable<Area> findAllByReservable(boolean reservable);

	/**
	 * Finds all reservable areas that fit the given criteria.
	 *
	 * @param capacity the minimum capacity of the area
	 * @param superArea the super area to search in
	 * @param areaType the type of area to search for
	 * @param areaFeatures the features of the area to search for
	 * @param featureCount the number of features to search for
	 * @return a list of areas that fit the given criteria
	 */
	@Query(value = """
		SELECT area
		FROM Area area
		LEFT JOIN area.features areaFeature
		WHERE area.reservable = true
		AND (:freeAreas IS NULL OR area IN (:freeAreas))
		AND (:capacity IS NULL OR area.capacity >= :capacity)
		AND (:superArea IS NULL OR area.superArea = :superArea)
		AND (:areaType IS NULL OR area.areaType = :areaType)
		AND (:features IS NULL OR areaFeature IN (:features))
		GROUP BY area
		HAVING (:featureCount IS NULL OR COUNT(areaFeature) >= :featureCount)
		""")
	Iterable<Area> searchWithOptionalParams(
		@Param("capacity") Integer capacity,
		@Param("superArea") Area superArea,
		@Param("areaType") AreaType areaType,
		@Param("features") List<AreaFeature> areaFeatures,
		@Param("freeAreas") List<Area> freeAreas,
		@Param("featureCount") Integer featureCount
	);
}
