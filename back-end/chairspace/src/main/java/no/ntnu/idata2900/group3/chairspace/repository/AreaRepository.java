package no.ntnu.idata2900.group3.chairspace.repository;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
	 * @param areaType the type of area to search for
	 * @param areaFeatures the features of the area to search for
	 * @param startDateTime the time to start search from
	 * @param endDateTime the time to end search from
	 * @return a list of areas that fit the given criteria
	 */
	@Query("""
		SELECT area
		FROM Area area

		""")
	Iterable<Area> searchWithOptionalParams(
		int capacity,
		String areaType,
		String areaFeatures,
		String startDateTime,
		String endDateTime
	);
}
