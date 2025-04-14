package no.ntnu.idata2900.group3.chairspace.repository;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
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
}
