package no.ntnu.idata2900.group3.chairspace.assembler;

import no.ntnu.idata2900.group3.chairspace.dto.SimpleUser;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import org.springframework.stereotype.Component;

/**
 * Utility class to help convert between simple and domain users.
 */
@Component
public class UserAssembler {

	/**
	 * Assembles a simple user from a regular user.
	 *
	 * @param user the regular user to assemble from
	 * @return the assembled simple user
	 */
	public SimpleUser toSimple(User user) {
		return new SimpleUser(
			user.getId(),
			user.getExternalId(),
			user.getName(),
			user.getEmail(),
			user.isAdmin()
		);
	}
}
