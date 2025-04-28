package no.ntnu.idata2900.group3.chairspace.service;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with and managing {@link User}s.
 *
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@Service
public class UserService extends EntityService<User, UUID> {

	/**
	 * Creates a new user service.
	 *
	 * @param repository autowired UserRepository
	 */
	public UserService(UserRepository repository) {
		super(repository);
	}
}
