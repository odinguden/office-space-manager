package no.ntnu.idata2900.group3.chairspace.repository;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Account entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {


	/**
	 * Finds the user based on the given external ID.
	 *
	 * @param externalId the external ID of the user
	 * @return the user with the given external ID, or null if not found
	 */
	public User findByExternalId(String externalId);
}