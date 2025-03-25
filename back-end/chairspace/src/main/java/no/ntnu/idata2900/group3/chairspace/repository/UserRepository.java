package no.ntnu.idata2900.group3.chairspace.repository;

import java.util.List;
import java.util.UUID;

import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Account entity.
 */
@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

	/**
	 * 
	 * @return
	 */
	@Query(value = """
		SELECT area.*
		FROM areas area
		JOIN app_data.area_administrators areaAdministrator
			ON areaAdministrator.area_id = area.area_id
		WHERE areaAdministrator.user_id = ?1
		""",
		nativeQuery = true
		)
	List<Area> findAreasOfUser(UUID id);
}